package com.mpt.journal.controller;

import com.mpt.journal.domain.entity.FilterEntity;
import com.mpt.journal.domain.service.FiltersService;
import com.mpt.journal.domain.service.RecipesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class FiltersController {

    @Autowired
    private RecipesService recipesService;

    @Autowired
    private FiltersService filtersService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'EDITOR')")
    @GetMapping("/filters/delete/{id}")
    public String deleteFilter(
            Model model,
            @PathVariable UUID id
    ) {
        filtersService.deleteFilter(id);
        return "redirect:/ingredients";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/filters")
    public String getRecipeFilters(
            Model model,
            @RequestParam(name = "recipe_id", required = false) UUID recipeID,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "show_deleted", required = false) Boolean showDeleted
    ) {
        var filters = recipeID == null ? filtersService.getFiltersList(page, pageSize, name, showDeleted) :
                filtersService.getFiltersByRecipe(page, pageSize, recipeID, name, showDeleted);
        var allowedRecipes = recipesService.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue();

        model.addAttribute("recipe_id", recipeID);
        model.addAttribute("name", name);
        model.addAttribute("show_deleted", showDeleted);

        model.addAttribute("filters", filters);
        model.addAttribute("allowed_recipes", allowedRecipes);

        return "filters/filters";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'EDITOR')")
    @GetMapping("/filters/add")
    public String addFilter(
            Model model
    ) {
        var recipes = recipesService.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null);
        model.addAttribute("allowed_recipes", recipes.getValue());
        model.addAttribute("filter_model", new FilterEntity());
        return "filters/createFilter";
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'EDITOR')")
    @PostMapping("/filters/add")
    public String addFilter(
            @ModelAttribute FilterEntity filter,
            BindingResult bindingResult,
            Model model
    ) {
        if (!bindingResult.hasErrors())
            filtersService.addFilter(filter);
        return "redirect:/filters";
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'EDITOR')")
    @GetMapping("/filters/update/{id}")
    public String editFilter(
            Model model,
            @PathVariable UUID id
    ) {
        var filter = filtersService.getFilterByID(id);
        if (filter == null)
            return "redirect:/filters";

        var recipes = recipesService.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null);
        model.addAttribute("allowed_recipes", recipes.getValue());
        model.addAttribute("filter_model", filter);
        return "filters/editFilter";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'EDITOR')")
    @PostMapping("/filters/update/{id}")
    public String editFilter(
            Model model,
            @ModelAttribute FilterEntity filter,
            BindingResult bindingResult
    ) {
        if (!bindingResult.hasErrors())
            filtersService.addFilter(filter);

        return "redirect:/filters";
    }
}
