package com.collegefest.management.web;

import com.collegefest.management.entity.Score;
import com.collegefest.management.exception.InvalidScoreException;
import com.collegefest.management.service.JudgeService;
import com.collegefest.management.service.ScoreService;
import com.collegefest.management.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/scores")
public class ScoreWebController {

    private final ScoreService scoreService;
    private final TeamService teamService;
    private final JudgeService judgeService;

    @Autowired
    public ScoreWebController(ScoreService scoreService, TeamService teamService, JudgeService judgeService) {
        this.scoreService = scoreService;
        this.teamService = teamService;
        this.judgeService = judgeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Scores");
        model.addAttribute("scores", scoreService.getAllScores());
        return "scores";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("pageTitle", "Submit Score");
        model.addAttribute("score", new Score());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("judges", judgeService.getAllJudges());
        return "score-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("score") Score score,
                          BindingResult bindingResult,
                          @RequestParam Long teamId,
                          @RequestParam Long judgeId,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Submit Score");
            model.addAttribute("teams", teamService.getAllTeams());
            model.addAttribute("judges", judgeService.getAllJudges());
            return "score-form";
        }
        try {
            scoreService.submitScore(score, teamId, judgeId);
            redirectAttributes.addFlashAttribute("success", "Score submitted successfully");
            return "redirect:/scores";
        } catch (InvalidScoreException ex) {
            model.addAttribute("pageTitle", "Submit Score");
            model.addAttribute("teams", teamService.getAllTeams());
            model.addAttribute("judges", judgeService.getAllJudges());
            model.addAttribute("error", ex.getMessage());
            return "score-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        scoreService.deleteScore(id);
        redirectAttributes.addFlashAttribute("success", "Score deleted successfully");
        return "redirect:/scores";
    }
}
