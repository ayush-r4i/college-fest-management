package com.collegefest.management.web;

import com.collegefest.management.entity.Event;
import com.collegefest.management.service.EventService;
import com.collegefest.management.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class LeaderboardWebController {

    private final ScoreService scoreService;
    private final EventService eventService;

    @Autowired
    public LeaderboardWebController(ScoreService scoreService, EventService eventService) {
        this.scoreService = scoreService;
        this.eventService = eventService;
    }

    @GetMapping("/leaderboard")
    public String leaderboard(@RequestParam(name = "eventId", required = false) Long eventId, Model model) {
        model.addAttribute("pageTitle", "Leaderboard");
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("selectedEventId", eventId);

        if (eventId != null) {
            Event selectedEvent = null;
            try {
                selectedEvent = eventService.getEventById(eventId);
            } catch (Exception ignored) {
                // If invalid event id, selectedEvent remains null
            }

            if (selectedEvent != null) {
                model.addAttribute("selectedEvent", selectedEvent);
                model.addAttribute("leaderboard", scoreService.getLeaderboardByEventId(eventId));
            } else {
                model.addAttribute("leaderboard", Collections.emptyList());
            }
        } else {
            model.addAttribute("leaderboard", Collections.emptyList());
        }
        return "leaderboard";
    }
}
