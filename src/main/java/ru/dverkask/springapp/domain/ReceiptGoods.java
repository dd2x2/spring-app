package ru.dverkask.springapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "receipt_goods")
@Setter
@Getter
public class ReceiptGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    @Column
    private int count;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;
}
