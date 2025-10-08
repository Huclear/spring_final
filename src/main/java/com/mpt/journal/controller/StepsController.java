package com.mpt.journal.controller;

import com.mpt.journal.domain.entity.IngredientEntity;
import com.mpt.journal.domain.entity.StepEntity;
import com.mpt.journal.domain.service.RecipesService;
import com.mpt.journal.domain.service.StepsService;
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
public class StepsController {
    @Autowired
    private RecipesService _recipes;

    @Autowired
    private StepsService _steps;

    @Autowired
    private UsersService _users;

    @GetMapping("/steps")
    public String getSteps(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "recipe_id", required = false) UUID recipe_id,
            @RequestParam(name = "from_order", required = false) Integer fromOrder,
            @RequestParam(name = "to_order", required = false) Integer toOrder,
            @RequestParam(name = "time_from", required = false) Double timeFrom,
            @RequestParam(name = "time_to", required = false) Double timeTo,
            Model model
    ) {
        var steps = recipe_id == null
                ? _steps.getStepsList(page, pageSize, fromOrder, toOrder, timeFrom, timeTo)
                : _steps.getStepsByRecipe(page, pageSize, recipe_id, fromOrder, toOrder, timeFrom, timeTo);
        var recipes = _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue();

        model.addAttribute("recipe_id", recipe_id);
        model.addAttribute("from_order", fromOrder);
        model.addAttribute("to_order", toOrder);
        model.addAttribute("time_from", timeFrom);
        model.addAttribute("time_to", timeTo);

        model.addAttribute("steps", steps);
        model.addAttribute("allowed_recipes", recipes);

        return "steps/list";
    }

    @GetMapping("steps/add")
    public String addRecipe(
            Model model
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = _users.getUserByLogin(auth.getName());
        var recipes = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                : _recipes.getRecipesByUser(1, Integer.MAX_VALUE, user.getId(), null, null, null, null, null, null).getValue();

        model.addAttribute("step_model", new StepEntity());
        model.addAttribute("allowed_recipes", recipes);
        return "steps/create";
    }

    @PostMapping("steps/add")
    public String postStep(
            @ModelAttribute StepEntity step,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (!bindingResult.hasErrors()) {
            if (auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                    step.getRecipe().getUser() == null ||
                    !step.getRecipe().getUser().getLogin().equals(auth.getName())) {
                attributes.addAttribute("error_message", "Not permitted");
                return "redirect:/steps";
            }
            _steps.addStep(step);
        }
        return "redirect:/steps";
    }


    @GetMapping("/steps/edit/{id}")
    public String editStep(
            Model model,
            @PathVariable UUID id,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var step = _steps.getStepByID(id);
        if (step == null)
            return "redirect:/steps";
        else if (
                auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                        step.getRecipe().getUser() == null ||
                        !step.getRecipe().getUser().getLogin().equals(auth.getName())
        ) {
            attributes.addAttribute("error_message", "Not permitted");
            return "redirect:/steps";
        }


        var user = _users.getUserByLogin(auth.getName());
        var recipes = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                : _recipes.getRecipesByUser(1, Integer.MAX_VALUE, user.getId(), null, null, null, null, null, null).getValue();

        model.addAttribute("step_model", step);
        model.addAttribute("allowed_recipes", recipes);
        return "ingredients/edit";
    }

    @PostMapping("/ingredients/edit")
    public String editStep(
            Model model,
            @ModelAttribute StepEntity step,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (!bindingResult.hasErrors()) {
            if (auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                    step.getRecipe().getUser() == null ||
                    !step.getRecipe().getUser().getLogin().equals(auth.getName())) {
                attributes.addAttribute("error_message", "Not permitted");
                return "redirect:/steps";
            }
            _steps.editStep(step);
        }

        return "redirect:/steps";
    }

    @GetMapping("/steps/delete/{id}")
    public String deleteStep(
            @PathVariable UUID id,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var ingredient = _steps.getStepByID(id);
        if (auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals(("ADMIN"))) &&
                (ingredient == null || ingredient.getRecipe() == null ||
                        ingredient.getRecipe().getUser() == null ||
                        !ingredient.getRecipe().getUser().getLogin().equals(auth.getName()))) {
            attributes.addAttribute("error_message", "Not permitted");
            return "redirect:/steps";
        }

        _steps.deleteStep(id);
        return "redirect:/steps";
    }
}
