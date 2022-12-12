package com.abn.assignment.recipes.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Recipe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private Boolean isFavorite;
    private Integer servingsQuantity;
    @ManyToMany
    @ToString.Exclude
    private Set<Ingredient> ingredients;
    private String instructions;
    private Boolean vegetarian;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Recipe recipe = (Recipe) o;
        return id != null && Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
