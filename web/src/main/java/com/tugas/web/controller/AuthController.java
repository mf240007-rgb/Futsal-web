package com.tugas.web.controller;

import com.tugas.web.service.AppDataService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AppDataService appDataService;

    public AuthController(AppDataService appDataService) {
        this.appDataService = appDataService;
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        if (session.getAttribute("username") != null) {
            if ("ADMIN".equals(session.getAttribute("userRole"))) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/proses-login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {
        if ("admin" .equals(username) && "admin123" .equals(password)) {
            session.setAttribute("username", username);
            session.setAttribute("userRole", "ADMIN");
            return "redirect:/admin/dashboard";
        }

        var userOpt = appDataService.findUser(username, password);
        if (userOpt.isPresent()) {
            session.setAttribute("username", username);
            session.setAttribute("userRole", "USER");
            return "redirect:/dashboard";
        }

        model.addAttribute("errorMessage", "Username atau password salah.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
