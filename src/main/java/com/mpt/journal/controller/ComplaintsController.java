package com.mpt.journal.controller;

import com.mpt.journal.domain.entity.ComplaintEntity;
import com.mpt.journal.domain.model.ComplaintStatus;
import com.mpt.journal.domain.service.ComplaintsService;
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

@Controller
public class ComplaintsController {

    @Autowired
    private ComplaintsService _complaints;

    @Autowired
    private UsersService _users;

    @Autowired
    private RecipesService _recipes;

    @GetMapping("/complaints")
    public String getComplaints(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(name = "user", required = false) UUID user_id,
            @RequestParam(name = "recipe", required = false) UUID recipe_id,
            @RequestParam(name = "topic", required = false) String topic,
            @RequestParam(name = "status", required = false) ComplaintStatus status,
            Model model
    ) {
        var complaints = _complaints.getComplaintsList(page, pageSize, recipe_id, user_id, topic, status);

        var allowedUsers = _users.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue();
        var allowedRecipes = _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue();

        model.addAttribute("complaints", complaints);
        model.addAttribute("allowed_users", allowedUsers);
        model.addAttribute("allowed_recipes", allowedRecipes);
        model.addAttribute("statuses", ComplaintStatus.values());
        model.addAttribute("user", user_id);
        model.addAttribute("recipe", recipe_id);
        model.addAttribute("topic", topic);
        model.addAttribute("status", status);

        return "complaints/list";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("complaints/add")
    public String getAddComplaint(
            Model model
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var allowedUsers = _users.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue()
                .stream().filter(u -> !u.getLogin().equals(auth.getName())).toList();
        var allowedRecipes = _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                .stream().filter(r -> !r.getUser().getLogin().equals(auth.getName())).toList();

        model.addAttribute("allowed_users", allowedUsers);
        model.addAttribute("allowed_recipes", allowedRecipes);
        model.addAttribute("complaint_model", new ComplaintEntity());

        return "complaints/create";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("complaints/add")
    public String postComplaint(
            @ModelAttribute ComplaintEntity complaint,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error_message", "Invalid model");
            return "redirect:/complaints";
        }

        complaint.setUser_sender(_users.getUserByLogin(auth.getName()));

        _complaints.addComplaint(complaint);
        return "redirect:/complaints";
    }

    @GetMapping("complaints/edit/{id}")
    public String getEditComplaint(
            @PathVariable UUID id,
            RedirectAttributes attributes,
            Model model
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var complaint = _complaints.getComplaintByID(id);
        if (complaint == null) {
            attributes.addAttribute("error_message", "Not found");
            return "redirect:/complaints";
        } else if (auth.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("USER")) &&
                !complaint.getUser_sender().getLogin().equals(auth.getName())) {
            attributes.addAttribute("error_message", "Not permitted");
            return "redirect:/complaints";
        }

        var allowedUsers = _users.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue()
                .stream().filter(u -> !u.getLogin().equals(auth.getName())).toList();
        var allowedRecipes = _recipes.getRecipes(1, Integer.MAX_VALUE, null, null, null, null, null, null).getValue()
                .stream().filter(r -> !r.getUser().getLogin().equals(auth.getName())).toList();

        model.addAttribute("allowed_users", allowedUsers);
        model.addAttribute("allowed_recipes", allowedRecipes);
        model.addAttribute("complaint_model", complaint);
        model.addAttribute("statuses", ComplaintStatus.values());

        if (auth.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("EDITOR")))
            return "complaints/edit_Editor";
        return "complaints/edit";
    }

    @PostMapping("complaints/edit")
    public String putComplaint(
            @ModelAttribute ComplaintEntity complaint,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error_message", "Invalid model");
            return "redirect:/complaints";
        }

        _complaints.editComplaint(complaint);
        return "redirect:/complaints";
    }

    @GetMapping("complaints/delete/{id}")
    public String deleteComplaint(
            @PathVariable UUID id,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var complaint = _complaints.getComplaintByID(id);
        if (complaint == null) {
            attributes.addAttribute("error_message", "Not found");
            return "redirect:/complaints";
        } else if (auth.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("USER")) &&
                !complaint.getUser_sender().getLogin().equals(auth.getName())) {
            attributes.addAttribute("error_message", "Not permitted");
            return "redirect:/complaints";
        }

        _complaints.deleteComplaint(id);
        return "redirect:/complaints";
    }
}
