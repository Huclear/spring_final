package com.mpt.journal.domain.service;

import com.mpt.journal.domain.entity.FilterEntity;
import com.mpt.journal.domain.entity.RecipeEntity;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.model.RecipesIngredientsFiltering;

import java.util.List;
import java.util.UUID;

public interface RecipesService {
    PagedResult<RecipeEntity> getRecipes(
            int page,
            int pageSize,
            String name,
            List<RecipesIngredientsFiltering> ingredients,
            List<FilterEntity> filters,
            Double timeFrom,
            Double timeTo,
            Boolean showDeleted
    );

    PagedResult<RecipeEntity> getRecipesByUser(
            int page,
            int pageSize,
            UUID userID,
            String name,
            List<RecipesIngredientsFiltering> ingredients,
            List<FilterEntity> filters,
            Double timeFrom,
            Double timeTo,
            Boolean showDeleted
    );

    RecipeEntity getRecipeById(UUID recipeID);

    RecipeEntity addRecipe(RecipeEntity recipe);

    RecipeEntity editRecipe(RecipeEntity recipe);

    void deleteRecipe(UUID recipeID);

    void deleteRecipes(List<UUID> recipeIDs);

    void confirmDeleteRecipe(UUID recipeID);

    void confirmDeleteRecipes(List<UUID> recipeIDs);
}
