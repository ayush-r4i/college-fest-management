package com.collegefest.management.exception;

/** Thrown when submitted score values are invalid (outside the 0-10 range, missing, etc). */
public class InvalidScoreException extends RuntimeException {
    public InvalidScoreException(String message) {
        super(message);
    }
}
