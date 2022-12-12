package com.abn.assignment.recipes.model;

import com.abn.assignment.recipes.enums.MeasurementMetric;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Ingredient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private MeasurementMetric measurementMetric;
    @NotNull
    private boolean isMeat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Ingredient that = (Ingredient) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

