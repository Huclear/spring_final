package com.mpt.journal.controller;

import com.mpt.journal.domain.entity.UserEntity;
import com.mpt.journal.domain.service.EmailConfirmationsService;
import com.mpt.journal.domain.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;


@Controller
@PreAuthorize("hasAnyAuthority('ADMIN', 'EDITOR')")
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private EmailConfirmationsService emailsService;

    @GetMapping("/users")
    public String getUsers(
            Model model,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "nickname", required = false) String nickname,
            @RequestParam(name = "minRecipesCount", required = false) Integer minRecipesCount,
            @RequestParam(name = "maxRecipesCount", required = false) Integer maxRecipesCount,
            @RequestParam(name = "show_deleted", required = false) Boolean showDeleted
    ) {
        var users = usersService.getUsers(page, pageSize, nickname, minRecipesCount, maxRecipesCount, showDeleted);
        model.addAttribute("users", users);

        model.addAttribute("nickname", nickname);
        model.addAttribute("minRecipesCount", minRecipesCount);
        model.addAttribute("maxRecipesCount", maxRecipesCount);
        model.addAttribute("show_del", showDeleted);
        return "users/users";
    }

    @GetMapping("/users/add")
    public String addUser(
            Model model
    ) {
        model.addAttribute("user_model", new UserEntity());
        return "users/createUser";
    }

    @PostMapping("/users/add")
    public String addUser(
            @Valid @ModelAttribute UserEntity user, BindingResult bindingResult, Model model,
            RedirectAttributes attributes
    ) {

        if (!bindingResult.hasErrors())
            usersService.addUser(user);
        return "redirect:/users";
    }


    @GetMapping("/users/update/{id}")
    public String updateUser( @PathVariable UUID id,
            Model model) {
        var user = usersService.getUserById(id);
        if(user == null)
            return "redirect:/users";
        model.addAttribute("user_model", user);
        return "users/editUser";
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute UserEntity user,
                             BindingResult bindingResult,
                             Model model) {
        if (!bindingResult.hasErrors())
            usersService.editUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable UUID id) {
        usersService.deleteUser(id);
        return "redirect:/users";
    }
}
