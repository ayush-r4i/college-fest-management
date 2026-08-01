package com.collegefest.management.web;

import com.collegefest.management.entity.Admin;
import com.collegefest.management.exception.InvalidCredentialsException;
import com.collegefest.management.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthWebController {

    private final AdminService adminService;

    @Autowired
    public AuthWebController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String root(HttpSession session) {
        return session.getAttribute("admin") != null ? "redirect:/dashboard" : "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("admin") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                           @RequestParam String password,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
        try {
            Admin admin = adminService.authenticate(username, password);
            // Prevent session fixation: start a fresh session on successful login
            request.getSession(true).setAttribute("admin", admin.getUsername());
            return "redirect:/dashboard";
        } catch (InvalidCredentialsException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
