package ru.dverkask.springapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.dverkask.springapp.domain.Goods;
import ru.dverkask.springapp.domain.ShopGoods;

public interface ShopGoodsRepository extends CrudRepository<ShopGoods, Long> {
    ShopGoods findByGoods(Goods goods);
}
