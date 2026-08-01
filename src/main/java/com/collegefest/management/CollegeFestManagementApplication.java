package com.collegefest.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the College Fest & Hackathon Management System.
 *
 * This is a beginner-friendly Spring Boot MVC + REST project.
 * No Spring Security, no Lombok, no Docker, no microservices - just
 * plain Spring Boot, Spring Data JPA, Thymeleaf and MySQL.
 */
@SpringBootApplication
public class CollegeFestManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollegeFestManagementApplication.class, args);
    }
}
