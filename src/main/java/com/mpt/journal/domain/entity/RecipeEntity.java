package com.mpt.journal.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Collection;
import java.util.UUID;

@Entity(name = "recipes")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private UserEntity user;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9 ]{5,}")
    private String recipe_name;

    private String recipe_description;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double rating = 0.0;

    @NotNull
    private Boolean deleted = false;

    @ManyToMany
    @JoinTable(name = "recipes_filters",
            inverseJoinColumns = @JoinColumn(name = "filter_id"),
            joinColumns = @JoinColumn(name = "recipe_id"))
    private Collection<FilterEntity> filters;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private Collection<IngredientEntity> ingredients;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private Collection<StepEntity> steps;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private Collection<ReviewEntity> reviews;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_description() {
        return recipe_description;
    }

    public void setRecipe_description(String recipe_description) {
        this.recipe_description = recipe_description;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Collection<FilterEntity> getFilters() {
        return filters;
    }

    public void setFilters(Collection<FilterEntity> filters) {
        this.filters = filters;
    }

    public Collection<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public Collection<StepEntity> getSteps() {
        return steps;
    }

    public void setSteps(Collection<StepEntity> steps) {
        this.steps = steps;
    }

    public Collection<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(Collection<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public Double getRating() {
        return rating;
    }
}

