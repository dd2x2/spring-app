package ru.dverkask.springapp.domain.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.Order;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.repositories.OrderRepository;
import ru.dverkask.springapp.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Seller extends UserEntity {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public void saveOrder(Long id, List<Goods> goods) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден по id " + id)));
        if (user.isPresent()) {
            UserEntity userEntity = user.get();

            if (userEntity.getRoles().contains(Role.SELLER)) {
                Order order = new Order();

                order.setGoods(goods);
                order.setSellerId(userEntity.getId());
                order.setOrderTime(LocalDateTime.now());

                orderRepository.save(order);
            }
        }
    }
}
