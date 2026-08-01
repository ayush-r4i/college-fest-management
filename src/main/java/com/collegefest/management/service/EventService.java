package com.collegefest.management.service;

import com.collegefest.management.entity.Event;
import com.collegefest.management.exception.ResourceNotFoundException;
import com.collegefest.management.exception.VenueConflictException;
import com.collegefest.management.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Transactional
    public Event createEvent(Event event) {
        validateTimeWindow(event);
        checkVenueConflict(event, null);
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, Event updated) {
        Event existing = getEventById(id);
        validateTimeWindow(updated);
        checkVenueConflict(updated, id);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setVenue(updated.getVenue());
        existing.setCategory(updated.getCategory());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        existing.setMaxTeamSize(updated.getMaxTeamSize());
        return eventRepository.save(existing);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event existing = getEventById(id);
        eventRepository.delete(existing);
    }

    private void validateTimeWindow(Event event) {
        if (event.getStartTime() != null && event.getEndTime() != null
                && !event.getStartTime().isBefore(event.getEndTime())) {
            throw new VenueConflictException("Event start time must be before end time");
        }
    }

    /**
     * Core business rule: before creating/updating an event, make sure no other
     * event is already scheduled at the same venue during an overlapping time window.
     */
    private void checkVenueConflict(Event event, Long excludeId) {
        List<Event> conflicts = eventRepository.findConflictingEvents(
                event.getVenue(), event.getStartTime(), event.getEndTime(), excludeId);

        if (!conflicts.isEmpty()) {
            Event conflict = conflicts.get(0);
            throw new VenueConflictException(
                    "Venue conflict: \"" + conflict.getName() + "\" is already scheduled at " +
                            event.getVenue() + " from " + conflict.getStartTime() + " to " + conflict.getEndTime());
        }
    }
}
