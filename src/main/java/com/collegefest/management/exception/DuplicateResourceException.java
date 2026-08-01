package com.collegefest.management.exception;

/** Thrown when trying to create a resource that already exists (e.g. duplicate judge email). */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
