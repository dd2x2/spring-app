package ru.dverkask.springapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    public enum OrderStatus {
        ORDERED,
        COMPLETED,
        ACCEPTED,
        CANCELED,
        RETURNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderGoods> goodsWithCount = new ArrayList<>();
    private LocalDateTime orderTime;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
}
