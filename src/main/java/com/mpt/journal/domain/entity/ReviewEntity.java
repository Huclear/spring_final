package com.mpt.journal.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Entity(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private RecipeEntity recipe;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private UserEntity user;

    @Pattern(regexp = "[A-Za-z 0-9]{50,500}")
    private String text;

    @NotNull
    @Positive
    private Integer rating;

    @NotNull
    private Boolean deleted = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
