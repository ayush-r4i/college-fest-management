package com.collegefest.management.api;

import com.collegefest.management.entity.Judge;
import com.collegefest.management.service.JudgeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judges")
public class JudgeApiController {

    private final JudgeService judgeService;

    @Autowired
    public JudgeApiController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @GetMapping
    public List<Judge> getAll() {
        return judgeService.getAllJudges();
    }

    @GetMapping("/{id}")
    public Judge getOne(@PathVariable Long id) {
        return judgeService.getJudgeById(id);
    }

    @PostMapping
    public ResponseEntity<Judge> create(@Valid @RequestBody Judge judge) {
        Judge created = judgeService.createJudge(judge);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public Judge update(@PathVariable Long id, @Valid @RequestBody Judge judge) {
        return judgeService.updateJudge(id, judge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        judgeService.deleteJudge(id);
        return ResponseEntity.noContent().build();
    }
}
