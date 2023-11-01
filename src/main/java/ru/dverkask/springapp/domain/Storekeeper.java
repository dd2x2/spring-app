package ru.dverkask.springapp.domain;

import java.util.ArrayList;
import java.util.List;

public class Storekeeper {
    private List<Invoice> list = new ArrayList<>();
    public void acceptGoods(Goods goods) {

    }
    public void collectGoods(Invoice invoice) {
        list.add(invoice);
    }
    public void sendGoodsToStore(Goods goods) {

    }
}
