package com.collegefest.management.web;

import com.collegefest.management.entity.Judge;
import com.collegefest.management.exception.DuplicateResourceException;
import com.collegefest.management.service.JudgeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/judges")
public class JudgeWebController {

    private final JudgeService judgeService;

    @Autowired
    public JudgeWebController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Judges");
        model.addAttribute("judges", judgeService.getAllJudges());
        return "judges";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("pageTitle", "Add Judge");
        model.addAttribute("judge", new Judge());
        return "judge-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("judge") Judge judge,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Judge");
            return "judge-form";
        }
        try {
            judgeService.createJudge(judge);
            redirectAttributes.addFlashAttribute("success", "Judge added successfully");
            return "redirect:/judges";
        } catch (DuplicateResourceException ex) {
            model.addAttribute("pageTitle", "Add Judge");
            model.addAttribute("error", ex.getMessage());
            return "judge-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Edit Judge");
        model.addAttribute("judge", judgeService.getJudgeById(id));
        return "judge-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                          @Valid @ModelAttribute("judge") Judge judge,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Judge");
            return "judge-form";
        }
        try {
            judgeService.updateJudge(id, judge);
            redirectAttributes.addFlashAttribute("success", "Judge updated successfully");
            return "redirect:/judges";
        } catch (DuplicateResourceException ex) {
            judge.setId(id);
            model.addAttribute("pageTitle", "Edit Judge");
            model.addAttribute("error", ex.getMessage());
            return "judge-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        judgeService.deleteJudge(id);
        redirectAttributes.addFlashAttribute("success", "Judge deleted successfully");
        return "redirect:/judges";
    }
}
