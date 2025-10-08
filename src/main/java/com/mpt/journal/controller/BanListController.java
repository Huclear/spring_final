package com.mpt.journal.controller;

import com.mpt.journal.data.Paginator;
import com.mpt.journal.domain.entity.BanList;
import com.mpt.journal.domain.service.BanListService;
import com.mpt.journal.domain.service.RecipesService;
import com.mpt.journal.domain.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.UUID;

@PreAuthorize("hasAnyAuthority('ADMIN', 'EDITOR')")
@Controller
public class BanListController {

    @Autowired
    private BanListService _banList;

    @Autowired
    private UsersService _users;

    @GetMapping("/banList")
    public String getBanList(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(name = "is_permanent", required = false) Boolean isPermanent,
            @RequestParam(name = "user", required = false) UUID user_id,
            Model model
    ) {
        var ban_list = user_id == null
                ? _banList.getBanListsList(page, pageSize, isPermanent)
                : Paginator.paginate(Collections.singletonList(_banList.getBanListByUserID(user_id)), 1, 10);
        var allowedUsers = _users.getUsers(1, Integer.MAX_VALUE, null, null, null, null).getValue();

        model.addAttribute("is_permanent", isPermanent);
        model.addAttribute("user", user_id);
        model.addAttribute("users", allowedUsers);
        model.addAttribute("ban_list", ban_list);

        return "banList/list";
    }

    @GetMapping("/banList/edit")
    public String getEditBanList(
            @RequestParam(name = "user", required = false) UUID user_id,
            RedirectAttributes attributes,
            Model model
    ) {
        if (_users.getUserById(user_id) == null) {
            attributes.addAttribute("error_message", "User not found");
            return "redirect:/banList";
        }

        var banRec = _banList.getBanListByUserID(user_id);
        if (banRec == null) {
            banRec = new BanList();
            banRec.setUser(_users.getUserById(user_id));
        }

        model.addAttribute("ban_model", banRec);
        return "banList/edit";
    }

    @PostMapping("/banList/edit")
    public String editBanList(
            @ModelAttribute BanList banList,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addAttribute("error_message", "Incorrect model");
            return "redirect:/banList";
        }

        _banList.editBanList(banList);
        return "redirect:/banList";
    }

    @GetMapping("/banList/delete/{id}")
    public String deleteBan(
            @PathVariable UUID id,
            RedirectAttributes attributes
    ) {
        var ban = _banList.getBanListByID(id);

        if (ban == null) {
            attributes.addAttribute("error_message", "Not found");
            return "redirect:/banList";
        }

        _banList.deleteBanList(id);
        return "redirect:/banList";
    }
}
