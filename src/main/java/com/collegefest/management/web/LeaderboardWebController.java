package com.collegefest.management.web;

import com.collegefest.management.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderboardWebController {

    private final ScoreService scoreService;

    @Autowired
    public LeaderboardWebController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        model.addAttribute("pageTitle", "Leaderboard");
        model.addAttribute("leaderboard", scoreService.getLeaderboard());
        return "leaderboard";
    }
}
