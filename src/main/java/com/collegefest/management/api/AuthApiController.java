package com.collegefest.management.api;

import com.collegefest.management.entity.Admin;
import com.collegefest.management.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AdminService adminService;

    @Autowired
    public AuthApiController(AdminService adminService) {
        this.adminService = adminService;
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    /**
     * Example body: { "username": "admin", "password": "admin123" }
     * On success this also creates an HttpSession, so if you test this in
     * Postman with cookies enabled, the session cookie will be sent on
     * subsequent requests too.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        Admin admin = adminService.authenticate(request.username, request.password);
        servletRequest.getSession(true).setAttribute("admin", admin.getUsername());
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "username", admin.getUsername(),
                "fullName", admin.getFullName() == null ? "" : admin.getFullName()
        ));
    }
}
