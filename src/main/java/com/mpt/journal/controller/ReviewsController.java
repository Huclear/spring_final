package com.mpt.journal.controller;

import com.mpt.journal.domain.entity.RecipeEntity;
import com.mpt.journal.domain.entity.ReviewEntity;
import com.mpt.journal.domain.model.PagedResult;
import com.mpt.journal.domain.model.ReviewsGroups;
import com.mpt.journal.domain.service.RecipesService;
import com.mpt.journal.domain.service.ReviewsService;
import com.mpt.journal.domain.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.UUID;


@Controller
public class ReviewsController {

    @Autowired
    private ReviewsService _reviews;
    @Autowired
    private UsersService _users;
    @Autowired
    private RecipesService _recipes;

    @GetMapping("/reviews")
    public String getReviews(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "user_id", required = false) UUID user_id,
            @RequestParam(name = "recipe_id", required = false) UUID recipe_id,
            @RequestParam(name = "group", required = false) ReviewsGroups group,
            Model model
    ) {
        PagedResult<ReviewEntity> reviews = _reviews.getReviewsList(page, pageSize, recipe_id, user_id, group);


        var allowedUsers = _users.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue();
        var allowedRecipes = _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue();

        model.addAttribute("reviews", reviews);
        model.addAttribute("user_id", user_id);
        model.addAttribute("users", allowedUsers);
        model.addAttribute("recipe_id", recipe_id);
        model.addAttribute("recipes", allowedRecipes);
        model.addAttribute("group", group);
        model.addAttribute("groups", ReviewsGroups.values());
        return "reviews/list";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/reviews/add")
    public String addReview(
            Model model
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var users = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? _users.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue()
                : Collections.singletonList(_users.getUserByLogin(auth.getName()));
        var recipes =
                _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                        .stream().filter(it ->
                                auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN")) ||
                                        !(it.getUser().equals(_users.getUserByLogin(auth.getName()))));


        model.addAttribute("review_model", new ReviewEntity());
        model.addAttribute("allowed_users", users);
        model.addAttribute("allowed_recipes", recipes);
        return "reviews/create";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/reviews/add")
    public String postReview(
            @ModelAttribute ReviewEntity review,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        var user_reviews = _reviews.getReviewsList(1, Integer.MAX_VALUE, review.getRecipe().getId(), review.getUser().getId(), null).getValue();
        if (!user_reviews.isEmpty()) {
            attributes.addAttribute("error_message", "Already posted review to that recipe");
            return "redirect:/reviews";
        }

        if (!bindingResult.hasErrors())
            _reviews.addReview(review);
        return "redirect:/recipes";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/reviews/edit/{id}")
    public String editReview(
            @PathVariable UUID id,
            Model model,
            RedirectAttributes attributes
    ) {
        var review = _reviews.getReviewByID(id);
        if (review == null) {
            attributes.addAttribute("error_message", "ID " + id.toString() + " not found");
            return "redirect:/reviews";
        }

        var auth = SecurityContextHolder.getContext().getAuthentication();
        var users = auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))
                ? _users.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue()
                : Collections.singletonList(_users.getUserByLogin(auth.getName()));
        var recipes =
                _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                        .stream().filter(it ->
                                auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN")) ||
                                        !(it.getUser().equals(_users.getUserByLogin(auth.getName()))));


        model.addAttribute("review_model", review);
        model.addAttribute("allowedUsers", users);
        model.addAttribute("allowedRecipes", recipes);
        return "reviews/edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("reviews/edit")
    public String putReview(
            @ModelAttribute ReviewEntity review,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        var user_reviews = _reviews.getReviewsList(1, Integer.MAX_VALUE, review.getRecipe().getId(), review.getUser().getId(), null).getValue();
        if (!user_reviews.isEmpty()) {
            attributes.addAttribute("error_message", "Already posted review to that recipe");
            return "redirect:/reviews";
        }

        if (!bindingResult.hasErrors())
            _reviews.editReview(review);
        return "redirect:/recipes";
    }

    @GetMapping("reviews/delete/{id}")
    public String deleteReview(
            @PathVariable UUID id,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = _users.getUserByLogin(auth.getName());

        var review_toDelete = _reviews.getReviewByID(id);
        if (auth.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ADMIN")) &&
                !review_toDelete.getUser().equals(user)) {
            attributes.addAttribute("error_message", "Access denied");
            return "redirect:/reviews";
        }

        _reviews.deleteReview(id);
        return "redirect:/reviews";
    }
}
