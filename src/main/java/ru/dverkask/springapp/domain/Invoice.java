package ru.dverkask.springapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
@Getter
@AllArgsConstructor
public class Invoice {
    private List<Goods> listOfGoods;
}
