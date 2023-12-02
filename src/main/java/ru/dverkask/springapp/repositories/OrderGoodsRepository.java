package ru.dverkask.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dverkask.springapp.domain.Order;
import ru.dverkask.springapp.domain.OrderGoods;

import java.util.List;
import java.util.Optional;

public interface OrderGoodsRepository extends JpaRepository<OrderGoods, Long> {
    List<OrderGoods> findByOrderId(Long orderId);
    List<OrderGoods> findByOrderAndStatus(Order order, OrderGoods.GatherStatus status);
    List<OrderGoods> findByOrder(Order order);
    OrderGoods findByOrderAndGoodsId(Order order, Long id);
}
