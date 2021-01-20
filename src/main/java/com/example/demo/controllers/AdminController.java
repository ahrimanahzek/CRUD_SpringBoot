package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("user/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public String index(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();//get logged in username
        List<GrantedAuthority> list = (List<GrantedAuthority>) auth.getAuthorities();

        StringBuilder stringBuilder = new StringBuilder();
        for(GrantedAuthority el : list) {
            stringBuilder.append(el.getAuthority().toString()).append(" ");
        }

        model.addAttribute("authorityString", stringBuilder.toString());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.index());
        return "user/admin/index";
    }

    @GetMapping("/new")
    public String index(@ModelAttribute("user") User user){
        return "user/admin/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/user/admin/";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.show(id));
        return "user/admin/edit";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.show(id));
        return "user/admin/show";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("user") User user){
        userService.update(id, user);
        return "redirect:/user/admin/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id){
        userService.delete(id);
        return "redirect:/user/admin/";
    }
}
