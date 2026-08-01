package com.collegefest.management.exception;

/** Thrown when adding a participant would exceed the event's maximum team size. */
public class TeamCapacityExceededException extends RuntimeException {
    public TeamCapacityExceededException(String message) {
        super(message);
    }
}
