package com.collegefest.management.service;

import com.collegefest.management.entity.Admin;
import com.collegefest.management.exception.InvalidCredentialsException;
import com.collegefest.management.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * Very simple username/password check. Passwords are stored as plain text
     * (see Admin entity note) since this project does not use Spring Security.
     */
    public Admin authenticate(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!admin.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return admin;
    }
}
