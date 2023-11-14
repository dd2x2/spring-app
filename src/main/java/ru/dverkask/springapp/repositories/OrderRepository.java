package ru.dverkask.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.dverkask.springapp.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
