package ru.dverkask.springapp.domain.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dverkask.springapp.domain.*;
import ru.dverkask.springapp.repositories.OrderRepository;
import ru.dverkask.springapp.repositories.ShopGoodsRepository;
import ru.dverkask.springapp.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Seller extends UserEntity {
    private final OrderRepository orderRepository;
    private final ShopGoodsRepository shopGoodsRepository;
    private final UserRepository userRepository;

    public void saveOrder(Long id, List<Goods> goodsList) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден по id " + id)));
        if (user.isPresent()) {
            UserEntity userEntity = user.get();

            if (userEntity.getRoles().contains(Role.SELLER)) {
                Order order = new Order();

                order.setSellerId(userEntity.getId());
                order.setOrderTime(LocalDateTime.now());
                order.setStatus(Order.OrderStatus.ORDERED);

                for (Goods goods : goodsList) {
                    OrderGoods orderGoods = new OrderGoods();
                    orderGoods.setGoods(goods);
                    orderGoods.setCount(goods.getCount());
                    orderGoods.setOrder(order);
                    orderGoods.setStatus(OrderGoods.GatherStatus.NOT_GATHERED);

                    order.getGoodsWithCount().add(orderGoods);
                }

                orderRepository.save(order);
            }
        }
    }

    public List<ShopGoods> getAllProducts() {
        return (List<ShopGoods>) shopGoodsRepository.findAll();
    }
}
