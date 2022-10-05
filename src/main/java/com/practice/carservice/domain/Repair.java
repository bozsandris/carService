package com.practice.carservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "repairs")
public class Repair implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "repair", allowSetters = true)
    private Car car;

    @Column(name = "progress")
    private Integer progress;

    @ManyToMany
    @JoinTable(
            name = "repair_parts",
            joinColumns = @JoinColumn(name = "repair_id"),
            inverseJoinColumns = @JoinColumn(name = "carpart_id"))
    private Collection<CarPart> neededParts = new ArrayList<>();

    @Column(name = "issue")
    private String issue;

    @Transient
    private Long repairCost;

    public Repair(Car car, Integer progress, Collection<CarPart> neededParts, String issue) {
        this.car = car;
        this.progress = progress;
        this.neededParts = neededParts;
        this.issue = issue;
    }

    public Long getRepairCost() {
        return this.neededParts.stream()
                .map(CarPart::getPrice)
                .reduce(0L, Long::sum);
    }
}
