package com.mpt.journal.domain.model;

public class RecipesIngredientsFiltering {
    private String ingredientName;
    private Measure currentMeasure;
    private Double amountFrom;
    private Double amountTo;

    public RecipesIngredientsFiltering(String ingredientName, Measure currentMeasure, Double amountFrom, Double amountTo) {
        this.ingredientName = ingredientName;
        this.currentMeasure = currentMeasure;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
    }

    public RecipesIngredientsFiltering(String ingredientName) {
        this(ingredientName, null, null, null);
    }

    public Double getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(Double amountTo) {
        this.amountTo = amountTo;
    }

    public Double getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Double amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Measure getCurrentMeasure() {
        return currentMeasure;
    }

    public void setCurrentMeasure(Measure currentMeasure) {
        this.currentMeasure = currentMeasure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}
