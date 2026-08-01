package com.collegefest.management.exception;

/** Thrown when an event's venue and time window overlaps with another event already scheduled there. */
public class VenueConflictException extends RuntimeException {
    public VenueConflictException(String message) {
        super(message);
    }
}
