package ru.dverkask.springapp.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_goods")
@Setter
@Getter
public class OrderGoods {
    public enum GatherStatus {
        GATHERED,
        NOT_GATHERED,
        ACCEPTED,
        RETURNED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;
    @Column(name = "count", nullable = false)
    private Integer count;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private GatherStatus status;
}
