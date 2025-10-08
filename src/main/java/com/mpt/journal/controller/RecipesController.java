package com.mpt.journal.controller;

import com.mpt.journal.domain.entity.FilterEntity;
import com.mpt.journal.domain.model.Measure;
import com.mpt.journal.domain.entity.RecipeEntity;
import com.mpt.journal.domain.model.RecipesIngredientsFiltering;
import com.mpt.journal.domain.service.FiltersService;
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

import java.util.*;

@Controller
public class RecipesController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RecipesService recipesService;
    @Autowired
    private FiltersService filtersService;

    //    Первая часть говнокода
    private List<RecipesIngredientsFiltering> filtering = new ArrayList<>();

    @GetMapping("/recipes")
    public String getUsers(
            Model model,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "user_id", required = false) UUID user_id,
            @RequestParam(name = "time_from", required = false) Double time_from,
            @RequestParam(name = "time_to", required = false) Double time_to,
            @RequestParam(name = "filters", required = false) Collection<String> filters,
            @RequestParam(name = "show_deleted", required = false) Boolean showDeleted
    ) {
        var appliedFilters = filters == null ? new ArrayList<FilterEntity>() : filters.stream().map(f -> filtersService.getFilterByName(f)).toList();
        var recipes = user_id == null ? recipesService.getRecipes(page, pageSize, name, filtering, appliedFilters, time_from, time_to, showDeleted)
                : recipesService.getRecipesByUser(page, pageSize, user_id, name, filtering, appliedFilters, time_from, time_to, showDeleted);
        var allowed_filters = filtersService.getFiltersList(1, Integer.MAX_VALUE, null, null).getValue();

        model.addAttribute("recipes", recipes);
        model.addAttribute("name", name);
        model.addAttribute("user_id", user_id);
        model.addAttribute("ingredients", filtering);
        model.addAttribute("filters", allowed_filters);
        model.addAttribute("applied_filters", appliedFilters);
        model.addAttribute("time_to", time_to);
        model.addAttribute("time_from", time_from);

        model.addAttribute("measures", Measure.values());
        model.addAttribute("ingr_filter_model", new RecipesIngredientsFiltering(""));

        return "recipes/recipes";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/recipes/add")
    public String postRecipe(
            Model model
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var users = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? usersService.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue()
                : Collections.singletonList(usersService.getUserByLogin(auth.getName()));

        var filters = filtersService.getFiltersList(1, Integer.MAX_VALUE, null, null);
        model.addAttribute("recipe_model", new RecipeEntity());
        model.addAttribute("allowedUsers", users);
        model.addAttribute("allowed_filters", filters.getValue());
        return "recipes/createRecipe";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/recipes/add")
    public String postRecipe(
            @ModelAttribute RecipeEntity recipe,
            BindingResult bindingResult,
            Model model
    ) {
        if (!bindingResult.hasErrors())
            recipesService.addRecipe(recipe);
        return "redirect:/recipes";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/recipes/edit/{id}")
    public String editRecipe(
            @PathVariable UUID id,
            Model model,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var recipe = recipesService.getRecipeById(id);
        if (recipe == null ||
                auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                        (recipe.getUser() == null || !recipe.getUser().getLogin().equals(auth.getName()))) {
            attributes.addAttribute("error_message", "Not permitted");
            return "redirect:/recipes";
        }

        var allowed_filters = filtersService.getFiltersList(1, Integer.MAX_VALUE, null, null);
        var users = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? usersService.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue()
                : Collections.singletonList(usersService.getUserByLogin(auth.getName()));

        model.addAttribute("allowedUsers", users);
        model.addAttribute("allowed_filters", allowed_filters.getValue());
        model.addAttribute("recipe_model", recipe);
        return "recipes/editRecipe";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/recipes/edit")
    public String editRecipe(
            @ModelAttribute RecipeEntity recipe,
            BindingResult bindingResult,
            Model model
    ) {
        if (!bindingResult.hasErrors())
            recipesService.editRecipe(recipe);
        return "redirect:/recipes";
    }

    @GetMapping("/recipes/delete/{id}")
    public String deleteRecipe(
            Model model,
            @PathVariable UUID id,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var recipe = recipesService.getRecipeById(id);

        if (auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                (recipe == null || recipe.getUser() == null || !recipe.getUser().getLogin().equals(auth.getName()))) {
            attributes.addAttribute("error_message", "Not permitted");
            return "redirect:/recipes";
        }

        recipesService.deleteRecipe(id);
        return "redirect:/recipes";
    }


    //вторая чатсь говнокода
    @PostMapping("/recipes/addIngrFilter")
    public String addFilteringParam(
            Model model,
            @ModelAttribute RecipesIngredientsFiltering filter,
            RedirectAttributes attributes) {
        if (filtering.stream().anyMatch(inf ->
                inf.getIngredientName().equals(filter.getIngredientName()))) {
            attributes.addAttribute("ingredients", filtering);
            return "redirect:/recipes";
        }

        filtering.add(filter);
        return "redirect:/recipes";
    }

    @PostMapping("/recipes/removeIngrFilter")
    public String removeFilteringParam(
            Model model,
            @ModelAttribute(name = "removedFilter") int index) {
        if (filtering.size() > index && index >= 0)
            filtering.remove(index);
        return "redirect:/recipes";
    }
}
