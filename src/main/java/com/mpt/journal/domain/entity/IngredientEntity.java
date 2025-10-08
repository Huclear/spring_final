package com.mpt.journal.domain.entity;

import com.mpt.journal.domain.model.LocalizedName;
import com.mpt.journal.domain.model.Measure;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity(name = "ingredients")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private RecipeEntity recipe;

    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    private LocalizedName name = new LocalizedName(null, null);

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Measure measureType;

    @NotNull
    private Boolean deleted = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalizedName getName() {
        return name;
    }

    public void setName(LocalizedName name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Measure getMeasureType() {
        return measureType;
    }

    public void setMeasureType(Measure measureType) {
        this.measureType = measureType;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }
}
