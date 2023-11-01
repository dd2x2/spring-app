package ru.dverkask.springapp.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "goods")
@Getter
@Setter
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String manufacturer;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private int count;
    @Column(nullable = false)
    private float price;
}
