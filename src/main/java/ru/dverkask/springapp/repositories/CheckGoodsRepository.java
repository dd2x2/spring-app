package ru.dverkask.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dverkask.springapp.domain.ReceiptGoods;

public interface CheckGoodsRepository extends JpaRepository<ReceiptGoods, Long> {
}
