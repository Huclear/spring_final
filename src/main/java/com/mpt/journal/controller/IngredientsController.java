package com.mpt.journal.controller;

import com.mpt.journal.domain.entity.IngredientEntity;
import com.mpt.journal.domain.model.Measure;
import com.mpt.journal.domain.service.IngredientsService;
import com.mpt.journal.domain.service.RecipesService;
import com.mpt.journal.domain.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
@Controller
public class IngredientsController {

    @Autowired
    private IngredientsService _ingredients;

    @Autowired
    private RecipesService _recipes;

    @Autowired
    private UsersService _users;

    public IngredientsController(RecipesService recipes, IngredientsService ingredients) {
        _ingredients = ingredients;
        _recipes = recipes;
    }

    @GetMapping("/ingredients/delete/{id}")
    public String deleteIngredient(
            Model model,
            @PathVariable UUID id,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var ingredient = _ingredients.getIngredientByID(id);
        if (auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals(("ADMIN"))) &&
                (ingredient == null || ingredient.getRecipe() == null ||
                        ingredient.getRecipe().getUser() == null ||
                        !ingredient.getRecipe().getUser().getLogin().equals(auth.getName()))) {
            attributes.addAttribute("error_message", "Not permitted");
            return "redirect:/ingredients";
        }

        _ingredients.deleteIngredient(id);
        return "redirect:/ingredients";
    }

    @GetMapping("/ingredients")
    public String getRecipeIngredients(
            Model model,
            @RequestParam(name = "recipe_id", required = false) String recipeID,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "measure", required = false) Measure measure,
            @RequestParam(name = "show_deleted", required = false) Boolean showDeleted
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = _users.getUserByLogin(auth.getName());

        var ingredients = recipeID == null ? _ingredients.getIngredientsList(page, pageSize, name, measure, showDeleted) : _ingredients.getIngredientsByRecipe(page, pageSize, recipeID, name, measure, showDeleted);
        var recipes = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                : _recipes.getRecipesByUser(1, Integer.MAX_VALUE, user.getId(), null, null, null, null, null, null).getValue();

        model.addAttribute("ingredients", ingredients);
        model.addAttribute("selected_measure", measure);
        model.addAttribute("measures", Measure.values());
        model.addAttribute("searched_name", name);
        model.addAttribute("selected_recipe", recipeID);
        model.addAttribute("allowed_recipes", recipes);
        return "ingredients/ingredients";
    }

    @GetMapping("/ingredients/add")
    public String addIngredient(
            Model model
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = _users.getUserByLogin(auth.getName());
        var recipes = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                : _recipes.getRecipesByUser(1, Integer.MAX_VALUE, user.getId(), null, null, null, null, null, null).getValue();
        model.addAttribute("ingredient_model", new IngredientEntity());
        model.addAttribute("allowed_recipes", recipes);
        model.addAttribute("measures", Measure.values());
        return "ingredients/createIngredient";
    }


    @PostMapping("/ingredients/add")
    public String addIngredient(
            @ModelAttribute IngredientEntity ingredient,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (!bindingResult.hasErrors()) {
            if (auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                    ingredient.getRecipe().getUser() == null ||
                    !ingredient.getRecipe().getUser().getLogin().equals(auth.getName())) {
                attributes.addAttribute("error_message", "Not permitted");
                return "redirect:/ingredients";
            }
            _ingredients.addIngredient(ingredient);
        }
        return "redirect:/ingredients";
    }


    @GetMapping("/ingredients/update/{id}")
    public String editIngredient(
            Model model,
            @PathVariable UUID id,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var ingredient = _ingredients.getIngredientByID(id);
        if (ingredient == null)
            return "redirect:/ingredients";
        else if (
                auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                        ingredient.getRecipe().getUser() == null ||
                        !ingredient.getRecipe().getUser().getLogin().equals(auth.getName())
        ) {
            attributes.addAttribute("error_message", "Not permitted");
            return "redirect:/ingredients";
        }


        var user = _users.getUserByLogin(auth.getName());
        var recipes = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                : _recipes.getRecipesByUser(1, Integer.MAX_VALUE, user.getId(), null, null, null, null, null, null).getValue();

        model.addAttribute("ingredient_model", ingredient);
        model.addAttribute("allowed_recipes", recipes);
        model.addAttribute("measures", Measure.values());
        return "ingredients/editIngredient";
    }

    @PostMapping("/ingredients/update")
    public String editIngredient(
            Model model,
            @ModelAttribute IngredientEntity ingredient,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (!bindingResult.hasErrors()) {
            if (auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                    ingredient.getRecipe().getUser() == null ||
                    !ingredient.getRecipe().getUser().getLogin().equals(auth.getName())) {
                attributes.addAttribute("error_message", "Not permitted");
                return "redirect:/ingredients";
            }
            _ingredients.editIngredient(ingredient);
        }

        return "redirect:/ingredients";
    }
}

