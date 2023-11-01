package ru.dverkask.springapp.domain;

public class Seller {
    public void orderGoodsFromWarehouse(Goods goods) {

    }
    public void acceptGoodsFromWarehouse(Goods goods) {
        String name = goods.getName();
        String description = goods.getDescription();
    }
    public void sellGoodsToCustomer(Goods goods) {
        StringBuilder stringBuilder = new StringBuilder();

        int id = goods.getId();
    }
}
