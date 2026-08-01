package com.collegefest.management.web;

import com.collegefest.management.service.EventService;
import com.collegefest.management.service.JudgeService;
import com.collegefest.management.service.ParticipantService;
import com.collegefest.management.service.ScoreService;
import com.collegefest.management.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardWebController {

    private final EventService eventService;
    private final TeamService teamService;
    private final ParticipantService participantService;
    private final JudgeService judgeService;
    private final ScoreService scoreService;

    @Autowired
    public DashboardWebController(EventService eventService, TeamService teamService,
                                   ParticipantService participantService, JudgeService judgeService,
                                   ScoreService scoreService) {
        this.eventService = eventService;
        this.teamService = teamService;
        this.participantService = participantService;
        this.judgeService = judgeService;
        this.scoreService = scoreService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("eventCount", eventService.getAllEvents().size());
        model.addAttribute("teamCount", teamService.getAllTeams().size());
        model.addAttribute("participantCount", participantService.getAllParticipants().size());
        model.addAttribute("judgeCount", judgeService.getAllJudges().size());
        model.addAttribute("scoreCount", scoreService.getAllScores().size());
        model.addAttribute("leaderboard", scoreService.getLeaderboard().stream().limit(5).toList());
        return "dashboard";
    }
}
