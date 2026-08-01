package com.collegefest.management.exception;

/** Thrown when a requested entity (Event, Team, Participant, Judge, Score, Admin) does not exist. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
