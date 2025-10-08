package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.IngredientEntity;
import com.mpt.journal.domain.model.Measure;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.repository.IngredientsRepository;
import com.mpt.journal.domain.service.IngredientsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SomeIngredientsService implements IngredientsService {
    private final IngredientsRepository _ingredients;

    public SomeIngredientsService(IngredientsRepository ingredients) {
        _ingredients = ingredients;
    }

    @Override
    public PagedResult<IngredientEntity> getIngredientsList(int page, int pageSize, String name, Measure measure, Boolean showDeleted) {
        var ingredients = _ingredients.findAll()
                .stream().filter(ing ->
                        (name == null || ing.getName().equals(name))
                                && (measure == null || ing.getMeasureType() == measure)
                                && ing.getDeleted() == (showDeleted == null ? false : showDeleted)
                )
                .toList();
        return Paginator.paginate(ingredients, page, pageSize);
    }

    @Override
    public PagedResult<IngredientEntity> getIngredientsByRecipe(int page, int pageSize, String recipe_ID, String name, Measure measure, Boolean showDeleted) {
        var ingredients = _ingredients.findAll()
                .stream().filter(ing ->
                        (name == null || ing.getName().equals(name))
                                && (measure == null || ing.getMeasureType() == measure)
                                && ing.getDeleted() == (showDeleted == null ? false : showDeleted)
                )
                .toList();
        return Paginator.paginate(ingredients, page, pageSize);
    }

    @Override
    public IngredientEntity getIngredientByID(UUID ingredientID) {
        return _ingredients.findById(ingredientID).orElse(null);
    }

    @Override
    public IngredientEntity addIngredient(IngredientEntity ingredient) {
        return _ingredients.save(ingredient);
    }

    @Override
    public IngredientEntity editIngredient(IngredientEntity ingredient) {
        return _ingredients.save(ingredient);
    }

    @Override
    public void deleteIngredient(UUID ingredientID) {
        var ingredient = _ingredients.findById(ingredientID).orElse(null);
        if (ingredient == null)
            return;
        if (ingredient.getDeleted())
            confirmDeleteIngredient(ingredientID);
        else {
            ingredient.setDeleted(true);
            _ingredients.save(ingredient);
        }
    }

    @Override
    public void deleteIngredients(List<UUID> ingredientIDs) {
        ingredientIDs.forEach(this::deleteIngredient);
    }

    @Override
    public void confirmDeleteIngredient(UUID ingredientID) {
        _ingredients.deleteById(ingredientID);
    }

    @Override
    public void confirmDeleteIngredients(List<UUID> ingredientIDs) {
        ingredientIDs.forEach(this::confirmDeleteIngredient);
    }
}
