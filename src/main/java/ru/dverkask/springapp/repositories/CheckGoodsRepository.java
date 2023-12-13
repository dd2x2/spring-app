package ru.dverkask.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dverkask.springapp.domain.CheckGoods;

public interface CheckGoodsRepository extends JpaRepository<CheckGoods, Long> {
}
