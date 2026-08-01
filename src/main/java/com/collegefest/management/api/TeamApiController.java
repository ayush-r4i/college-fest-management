package com.collegefest.management.api;

import com.collegefest.management.entity.Team;
import com.collegefest.management.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamApiController {

    private final TeamService teamService;

    @Autowired
    public TeamApiController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<Team> getAll() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public Team getOne(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    /** Pass the target event as a query parameter, e.g. POST /api/teams?eventId=1 */
    @PostMapping
    public ResponseEntity<Team> create(@Valid @RequestBody Team team, @RequestParam Long eventId) {
        Team created = teamService.registerTeam(team, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public Team update(@PathVariable Long id, @Valid @RequestBody Team team) {
        return teamService.updateTeam(id, team);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
