package com.practice.carservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "car_parts")
public class CarPart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "price")
    private Long price;

    @ManyToMany(mappedBy = "neededParts")
    @JsonIgnore
    @ToString.Exclude
    private Collection<Repair> repairs = new ArrayList<>();

    public CarPart(String name, String brand, Long price) {
        this.name = name;
        this.brand = brand;
        this.price = price;
    }
}
