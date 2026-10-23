package com.collegefest.management.web;

import com.collegefest.management.entity.Participant;
import com.collegefest.management.exception.DuplicateResourceException;
import com.collegefest.management.exception.TeamCapacityExceededException;
import com.collegefest.management.service.ParticipantService;
import com.collegefest.management.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/participants")
public class ParticipantWebController {

    private final ParticipantService participantService;
    private final TeamService teamService;

    @Autowired
    public ParticipantWebController(ParticipantService participantService, TeamService teamService) {
        this.participantService = participantService;
        this.teamService = teamService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Participants");
        model.addAttribute("participants", participantService.getAllParticipants());
        return "participants";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam(name = "teamId", required = false) Long teamId, Model model) {
        model.addAttribute("pageTitle", "Add Participant");
        model.addAttribute("participant", new Participant());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("selectedTeamId", teamId);
        return "participant-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("participant") Participant participant,
                          BindingResult bindingResult,
                          @RequestParam Long teamId,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Participant");
            model.addAttribute("teams", teamService.getAllTeams());
            model.addAttribute("selectedTeamId", teamId);
            return "participant-form";
        }
        try {
            participantService.addParticipant(participant, teamId);
            redirectAttributes.addFlashAttribute("success", "Participant added successfully");
            return "redirect:/participants";
        } catch (DuplicateResourceException | TeamCapacityExceededException ex) {
            model.addAttribute("pageTitle", "Add Participant");
            model.addAttribute("teams", teamService.getAllTeams());
            model.addAttribute("selectedTeamId", teamId);
            model.addAttribute("error", ex.getMessage());
            return "participant-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Edit Participant");
        model.addAttribute("participant", participantService.getParticipantById(id));
        model.addAttribute("teams", teamService.getAllTeams());
        return "participant-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                          @Valid @ModelAttribute("participant") Participant participant,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Participant");
            model.addAttribute("teams", teamService.getAllTeams());
            return "participant-form";
        }
        participantService.updateParticipant(id, participant);
        redirectAttributes.addFlashAttribute("success", "Participant updated successfully");
        return "redirect:/participants";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        participantService.deleteParticipant(id);
        redirectAttributes.addFlashAttribute("success", "Participant deleted successfully");
        return "redirect:/participants";
    }
}
