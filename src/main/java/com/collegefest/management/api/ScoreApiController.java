package com.collegefest.management.api;

import com.collegefest.management.entity.Score;
import com.collegefest.management.service.ScoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreApiController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreApiController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping
    public List<Score> getAll() {
        return scoreService.getAllScores();
    }

    @GetMapping("/{id}")
    public Score getOne(@PathVariable Long id) {
        return scoreService.getScoreById(id);
    }

    /**
     * Submits (or updates, if this judge already scored this team) a score.
     * Example: POST /api/scores?teamId=1&judgeId=1
     * Body: { "innovation": 8, "technicalImplementation": 7.5, "presentation": 9,
     *          "uiUx": 8.5, "overallImpression": 9 }
     * finalScore is calculated automatically as the average of the five fields.
     */
    @PostMapping
    public ResponseEntity<Score> submit(@Valid @RequestBody Score score,
                                         @RequestParam Long teamId,
                                         @RequestParam Long judgeId) {
        Score saved = scoreService.submitScore(score, teamId, judgeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scoreService.deleteScore(id);
        return ResponseEntity.noContent().build();
    }
}
