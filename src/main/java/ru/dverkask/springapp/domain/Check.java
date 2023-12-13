package ru.dverkask.springapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "checks")
@Getter
@Setter
public class Check {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long sellerId;

    @Column
    private LocalDateTime sellTime;

    @OneToMany(mappedBy = "check", cascade = CascadeType.ALL)
    private List<CheckGoods> goods;
}
