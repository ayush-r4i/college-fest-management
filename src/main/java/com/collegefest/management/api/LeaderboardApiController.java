package com.collegefest.management.api;

import com.collegefest.management.dto.LeaderboardEntry;
import com.collegefest.management.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardApiController {

    private final ScoreService scoreService;

    @Autowired
    public LeaderboardApiController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping
    public List<LeaderboardEntry> getLeaderboard() {
        return scoreService.getLeaderboard();
    }
}
