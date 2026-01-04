package com.ismail.al_baraka.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ismail.al_baraka.dto.user.request.UserRequest;
import com.ismail.al_baraka.service.UserService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String role = authentication.getAuthorities().toString();
        if (role.contains("CLIENT")) return "redirect:/client/dashboard";
        if (role.contains("AGENT_BANCAIRE")) return "redirect:/agent/dashboard";
        if (role.contains("ADMIN")) return "redirect:/admin/dashboard";
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @ModelAttribute("userRequest") UserRequest request,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.createUser(request);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        return "redirect:/login?registered";
    }

    @GetMapping("/client/dashboard")
    public String clientDashboard(Model model, Authentication auth) {
        if(model == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", auth.getName());
        return "ClientDashboard";
    }

    @GetMapping("/agent/dashboard")
    public String agentDashboard(Model model, Authentication auth) {
        model.addAttribute("fullName", auth.getName());
        return "AgentDashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, Authentication auth) {
        model.addAttribute("fullName", auth.getName());
        return "AdminDashboard";
    }
}