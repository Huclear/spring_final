package com.mpt.journal.domain.service;

import com.mpt.journal.domain.entity.IngredientEntity;
import com.mpt.journal.domain.model.Measure;
import com.mpt.journal.domain.model.PagedResult;

import java.util.List;
import java.util.UUID;

public interface IngredientsService {
    PagedResult<IngredientEntity> getIngredientsList(
            int page,
            int pageSize,
            String name,
            Measure measure,
            Boolean showDeleted
    );

    PagedResult<IngredientEntity> getIngredientsByRecipe(
            int page,
            int pageSize,
            String recipe_ID,
            String name,
            Measure measure,
            Boolean showDeleted
    );

    IngredientEntity getIngredientByID(UUID ingredientID);

    IngredientEntity addIngredient(IngredientEntity ingredient);

    IngredientEntity editIngredient(IngredientEntity ingredient);

    void deleteIngredient(UUID ingredientID);

    void deleteIngredients(List<UUID> ingredientIDs);

    void confirmDeleteIngredient(UUID ingredientID);

    void confirmDeleteIngredients(List<UUID> ingredientIDs);
}
