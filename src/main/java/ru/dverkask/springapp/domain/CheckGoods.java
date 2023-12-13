package ru.dverkask.springapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "check_goods")
@Setter
@Getter
public class CheckGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "check_id")
    private Check check;

    @Column
    private int count;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;
}
