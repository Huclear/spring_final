package com.mpt.journal.domain.entity;

import com.mpt.journal.domain.model.LocalizedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Collection;
import java.util.UUID;

@Entity(name = "filters")
public class FilterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    private LocalizedName name = new LocalizedName(null, null);

    @ManyToMany
    @JoinTable(name = "recipes_filters",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private Collection<RecipeEntity> recipes;

    private Boolean deleted = false;

    public LocalizedName getName() {
        return name;
    }

    public void setName(LocalizedName name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Collection<RecipeEntity> getRecipes() {
        return recipes;
    }

    public void setRecipes(Collection<RecipeEntity> recipes) {
        this.recipes = recipes;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
