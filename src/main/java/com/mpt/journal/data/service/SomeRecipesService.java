package com.mpt.journal.data.service;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.FilterEntity;
import com.mpt.journal.domain.entity.IngredientEntity;
import com.mpt.journal.domain.entity.RecipeEntity;
import com.mpt.journal.domain.entity.StepEntity;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.model.RecipesIngredientsFiltering;
import com.mpt.journal.domain.repository.FilterRepository;
import com.mpt.journal.domain.repository.IngredientsRepository;
import com.mpt.journal.domain.repository.RecipesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class SomeRecipesService implements com.mpt.journal.domain.service.RecipesService {
    private final RecipesRepository _recipes;
    private final IngredientsRepository _ingredients;
    private final FilterRepository _filters;

    public SomeRecipesService(RecipesRepository recipes, IngredientsRepository ingredients, FilterRepository filters) {
        this._recipes = recipes;
        this._ingredients = ingredients;
        this._filters = filters;
    }


    @Override
    public PagedResult<RecipeEntity> getRecipes(
            int page,
            int pageSize,
            String name,
            List<RecipesIngredientsFiltering> ingredients,
            List<FilterEntity> filters,
            Double timeFrom,
            Double timeTo,
            Boolean showDeleted) {

        var recipesByName = _recipes.findAll().stream().filter(r ->

                r.getDeleted() == (showDeleted != null && showDeleted) &&
                        (name == null ||
                                r.getRecipe_name().toLowerCase().contains(name.toLowerCase())) &&
                        (r.getSteps() == null ||
                                ((timeFrom == null || r.getSteps().stream().mapToDouble(StepEntity::getDurationMinutes).sum() >= timeFrom) &&
                                        (timeTo == null || r.getSteps().stream().mapToDouble(StepEntity::getDurationMinutes).sum() <= timeTo))
                        )
        ).toList();

        if (ingredients != null && !ingredients.isEmpty())
            recipesByName = recipesByName
                    .stream().filter(r -> ingredients.stream().allMatch(ingF ->
                            r.getIngredients().stream().anyMatch(ing ->
                                    ing.getName().equals(ingF.getIngredientName())
                                            && (ingF.getCurrentMeasure() == null || ingF.getCurrentMeasure().equals(ing.getMeasureType()))
                                            && ing.getAmount() >= ingF.getAmountFrom() && ing.getAmount() <= ingF.getAmountTo()
                            ))
                    )
                    .toList();

        if (filters != null && !filters.isEmpty())
            recipesByName = recipesByName
                    .stream().filter(r -> r.getFilters().containsAll(filters))
                    .toList();


        return Paginator.paginate(recipesByName, page, pageSize);
    }

    @Override
    public PagedResult<RecipeEntity> getRecipesByUser(
            int page,
            int pageSize,
            UUID userID,
            String name,
            List<RecipesIngredientsFiltering> ingredients,
            List<FilterEntity> filters,
            Double timeFrom,
            Double timeTo,
            Boolean showDeleted) {
        var recipesByName = _recipes.findAll().stream().filter(r ->
                r.getUser().getId() == userID &&
                        r.getDeleted() == (showDeleted == null ? false : showDeleted) &&
                        (name == null ||
                                r.getRecipe_name().toLowerCase().contains(name.toLowerCase())) &&
                        (r.getSteps() == null ||
                                ((timeFrom == null || r.getSteps().stream().mapToDouble(StepEntity::getDurationMinutes).sum() >= timeFrom) &&
                                        (timeTo == null || r.getSteps().stream().mapToDouble(StepEntity::getDurationMinutes).sum() <= timeTo))
                        )
        ).toList();

        if (ingredients != null && !ingredients.isEmpty())
            recipesByName = recipesByName
                    .stream().filter(r -> ingredients.stream().allMatch(ingF ->
                            r.getIngredients().stream().anyMatch(ing ->
                                    ing.getName().equals(ingF.getIngredientName())
                                            && (ingF.getCurrentMeasure() == null || ingF.getCurrentMeasure().equals(ing.getMeasureType()))
                                            && ing.getAmount() >= ingF.getAmountFrom() && ing.getAmount() <= ingF.getAmountTo()
                            ))
                    )
                    .toList();

        if (filters != null && !filters.isEmpty())
            recipesByName = recipesByName
                    .stream().filter(r -> r.getFilters().containsAll(filters))
                    .toList();

        return Paginator.paginate(recipesByName, page, pageSize);
    }

    @Override
    public RecipeEntity getRecipeById(UUID recipeID) {
        return _recipes.findById(recipeID).orElse(null);
    }

    @Override
    public RecipeEntity addRecipe(RecipeEntity recipe) {
        return _recipes.save(recipe);
    }

    @Override
    public RecipeEntity editRecipe(RecipeEntity recipe) {
        return _recipes.save(recipe);
    }

    @Override
    public void deleteRecipe(UUID recipeID) {
        var recipe = _recipes.findById(recipeID).orElse(null);
        if (recipe == null)
            return;
        if (recipe.getDeleted())
            confirmDeleteRecipe(recipeID);
        else {
            recipe.setDeleted(true);
            _recipes.save(recipe);
        }
    }

    @Override
    public void deleteRecipes(List<UUID> recipeIDs) {
        recipeIDs.forEach(this::deleteRecipe);
    }

    @Override
    public void confirmDeleteRecipe(UUID recipeID) {
        _recipes.deleteById(recipeID);
    }

    @Override
    public void confirmDeleteRecipes(List<UUID> recipeIDs) {
        recipeIDs.forEach(this::confirmDeleteRecipe);
    }
}
