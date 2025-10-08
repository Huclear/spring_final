package com.mpt.journal.controller;

import com.mpt.journal.domain.entity.UserEntity;
import com.mpt.journal.domain.model.RegistrationModel;
import com.mpt.journal.domain.model.RoleEnum;
import com.mpt.journal.domain.repository.UsersRepository;
import com.mpt.journal.domain.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UsersService _users;

    @Autowired
    private PasswordEncoder _encoder;

    @GetMapping("/registration")
    public String regView(
            Model model,
            @RequestParam(name = "error_message", required = false) String error_text
    ) {
        model.addAttribute("register_model", new RegistrationModel());
        model.addAttribute("error_message", error_text);
        return "auth/signUp";
    }

    @PostMapping("/registration")
    public String register(
            @ModelAttribute RegistrationModel request,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {

        if (_users.getUserByLogin(request.getLogin()) != null) {
            attributes.addAttribute("error_message", "Пользователь с таким логином уже существует");
            return "redirect:/registration";
        } else if (bindingResult.hasErrors()) {
            attributes.addAttribute("error_message", "Validation Error");
            return "redirect:/registration";
        }
        var user = new UserEntity();
        user.setLogin(request.getLogin());
        user.setNickname(request.getNickname());
        user.setPassword(_encoder.encode(request.getPassword()));
        user.setAboutMe(request.getAboutMe());
        user.setRoles(Collections.singleton(RoleEnum.USER));
        _users.addUser(user);
        return "redirect:/login";
    }
}
