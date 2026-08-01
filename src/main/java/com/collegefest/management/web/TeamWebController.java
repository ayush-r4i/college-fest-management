package com.collegefest.management.web;

import com.collegefest.management.entity.Team;
import com.collegefest.management.exception.DuplicateResourceException;
import com.collegefest.management.exception.TeamCapacityExceededException;
import com.collegefest.management.service.EventService;
import com.collegefest.management.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teams")
public class TeamWebController {

    private final TeamService teamService;
    private final EventService eventService;

    @Autowired
    public TeamWebController(TeamService teamService, EventService eventService) {
        this.teamService = teamService;
        this.eventService = eventService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Teams");
        model.addAttribute("teams", teamService.getAllTeams());
        return "teams";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("pageTitle", "Register Team");
        model.addAttribute("team", new Team());
        model.addAttribute("events", eventService.getAllEvents());
        return "team-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("team") Team team,
                          BindingResult bindingResult,
                          @RequestParam Long eventId,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Register Team");
            model.addAttribute("events", eventService.getAllEvents());
            return "team-form";
        }
        try {
            teamService.registerTeam(team, eventId);
            redirectAttributes.addFlashAttribute("success", "Team registered successfully");
            return "redirect:/teams";
        } catch (DuplicateResourceException | TeamCapacityExceededException ex) {
            model.addAttribute("pageTitle", "Register Team");
            model.addAttribute("events", eventService.getAllEvents());
            model.addAttribute("error", ex.getMessage());
            return "team-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Edit Team");
        model.addAttribute("team", teamService.getTeamById(id));
        model.addAttribute("events", eventService.getAllEvents());
        return "team-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                          @Valid @ModelAttribute("team") Team team,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Team");
            model.addAttribute("events", eventService.getAllEvents());
            return "team-form";
        }
        teamService.updateTeam(id, team);
        redirectAttributes.addFlashAttribute("success", "Team updated successfully");
        return "redirect:/teams";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        teamService.deleteTeam(id);
        redirectAttributes.addFlashAttribute("success", "Team deleted successfully");
        return "redirect:/teams";
    }
}
