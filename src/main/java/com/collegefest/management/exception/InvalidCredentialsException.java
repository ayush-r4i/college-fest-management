package com.collegefest.management.exception;

/** Thrown when an admin login attempt uses a wrong username/password combination. */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
