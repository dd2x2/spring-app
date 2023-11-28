package ru.dverkask.springapp.domain.entity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.Order;
import ru.dverkask.springapp.domain.OrderGoods;
import ru.dverkask.springapp.repositories.GoodsRepository;
import ru.dverkask.springapp.repositories.OrderGoodsRepository;
import ru.dverkask.springapp.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

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
                                "На складе нет необходимого количества товаров (на складе: %d, в заказе: %d)",
                                goods.getCount(), item.getCount()));
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
}
