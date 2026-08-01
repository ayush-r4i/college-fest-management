package com.collegefest.management.repository;

import com.collegefest.management.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByVenueIgnoreCase(String venue);

    /**
     * Finds events at the same venue whose time range overlaps with the given
     * [startTime, endTime) window, excluding the event currently being edited (if any).
     * Two ranges [s1,e1) and [s2,e2) overlap when s1 < e2 AND s2 < e1.
     */
    @Query("SELECT e FROM Event e WHERE LOWER(e.venue) = LOWER(:venue) " +
           "AND (:excludeId IS NULL OR e.id <> :excludeId) " +
           "AND e.startTime < :endTime AND :startTime < e.endTime")
    List<Event> findConflictingEvents(@Param("venue") String venue,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       @Param("excludeId") Long excludeId);
}
