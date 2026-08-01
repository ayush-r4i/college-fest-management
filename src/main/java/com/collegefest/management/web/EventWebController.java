package com.collegefest.management.web;

import com.collegefest.management.entity.Event;
import com.collegefest.management.exception.VenueConflictException;
import com.collegefest.management.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/events")
public class EventWebController {

    private final EventService eventService;

    @Autowired
    public EventWebController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Events");
        model.addAttribute("events", eventService.getAllEvents());
        return "events";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("pageTitle", "Add Event");
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("event") Event event,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Event");
            return "event-form";
        }
        try {
            eventService.createEvent(event);
            redirectAttributes.addFlashAttribute("success", "Event created successfully");
            return "redirect:/events";
        } catch (VenueConflictException ex) {
            model.addAttribute("pageTitle", "Add Event");
            model.addAttribute("error", ex.getMessage());
            return "event-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Edit Event");
        model.addAttribute("event", eventService.getEventById(id));
        return "event-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                          @Valid @ModelAttribute("event") Event event,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Event");
            return "event-form";
        }
        try {
            eventService.updateEvent(id, event);
            redirectAttributes.addFlashAttribute("success", "Event updated successfully");
            return "redirect:/events";
        } catch (VenueConflictException ex) {
            event.setId(id);
            model.addAttribute("pageTitle", "Edit Event");
            model.addAttribute("error", ex.getMessage());
            return "event-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("success", "Event deleted successfully");
        return "redirect:/events";
    }
}
