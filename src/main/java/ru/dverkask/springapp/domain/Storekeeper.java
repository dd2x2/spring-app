package ru.dverkask.springapp.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dverkask.springapp.repositories.GoodsRepository;
import ru.dverkask.springapp.repositories.OrderGoodsRepository;
import ru.dverkask.springapp.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class Storekeeper extends UserEntity {
    private OrderRepository orderRepository;
    private GoodsRepository goodsRepository;
    private OrderGoodsRepository orderGoodsRepository;

    public List<Order> getOrderedOrders() {
        return orderRepository.findAllByStatus(Order.OrderStatus.ORDERED);
    }

    public void cancelOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            Order newOrder = order.get();
            newOrder.setStatus(Order.OrderStatus.CANCELED);

            orderRepository.save(newOrder);
        }
    }

    public void completeOrder(Long id, List<Long> gatheredGoodsIds) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            List<OrderGoods> orderGoods = orderGoodsRepository.findByOrderId(id);

            for (OrderGoods item : orderGoods) {
                if (gatheredGoodsIds.contains(item.getGoods().getId())) {
                    Goods goods = item.getGoods();
                    int newGoodsCount = goods.getCount() - item.getCount();

                    if (newGoodsCount < 0) {
                        throw new IllegalArgumentException(String.format(
                                "Товара %s не хватает на складе (на складе: %d, в заказе: %d)",
                                goods.getName(), goods.getCount(), item.getCount()));
                    }
                }
            }

            Order newOrder = order.get();
            newOrder.setStatus(Order.OrderStatus.COMPLETED);
            orderRepository.save(newOrder);

            for (OrderGoods item : orderGoods) {
                if (gatheredGoodsIds.contains(item.getGoods().getId())) {
                    item.setStatus(OrderGoods.GatherStatus.GATHERED);
                    Goods goods = item.getGoods();

                    int newGoodsCount = goods.getCount() - item.getCount();
                    goods.setCount(newGoodsCount);
                    goodsRepository.save(goods);

                    item.setStatus(OrderGoods.GatherStatus.GATHERED);
                } else {
                    item.setStatus(OrderGoods.GatherStatus.NOT_GATHERED);
                }

                orderGoodsRepository.save(item);
            }
        }
    }

    public void returnGoods(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        List<OrderGoods> orderGoodsList = orderGoodsRepository.findByOrder(order);

        for (OrderGoods orderGoods : orderGoodsList) {
            Goods goods = orderGoods.getGoods();
            goods.setCount(goods.getCount() + orderGoods.getCount());
        }

        assert order != null;
        order.setStatus(Order.OrderStatus.RETURNED);
        orderRepository.save(order);

        goodsRepository.saveAll(orderGoodsList.stream().map(OrderGoods::getGoods).collect(Collectors.toList()));
    }
}
