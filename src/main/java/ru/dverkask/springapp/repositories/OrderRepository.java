package ru.dverkask.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.dverkask.springapp.domain.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(Order.OrderStatus status);
    List<Order> findByStatus(Order.OrderStatus status);
}
