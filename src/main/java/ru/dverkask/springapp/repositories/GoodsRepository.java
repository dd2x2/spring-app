package ru.dverkask.springapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.dverkask.springapp.domain.Goods;

public interface GoodsRepository extends CrudRepository<Goods, Long> {
    Goods findByNameAndManufacturer(String name, String manufacturer);
}
