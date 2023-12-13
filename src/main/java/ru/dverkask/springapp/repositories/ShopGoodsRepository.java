package ru.dverkask.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.ShopGoods;

public interface ShopGoodsRepository extends JpaRepository<ShopGoods, Long> {
    ShopGoods findByGoods(Goods goods);
}
