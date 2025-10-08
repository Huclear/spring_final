package com.mpt.journal.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Entity(name = "steps")
public class StepEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private RecipeEntity recipe;

    @NotNull
    @Positive
    private Integer step_order = 1;

    @Pattern(regexp = "[A-Za-z 0-9]+")
    private String description;

    @NotNull
    @Positive
    private Float durationMinutes;

    @NotNull
    private Boolean deleted = false;

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }

    public Integer getStep_order() {
        return step_order;
    }

    public void setStep_order(Integer step_order) {
        this.step_order = step_order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Float durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
