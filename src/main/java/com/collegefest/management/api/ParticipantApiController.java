package com.collegefest.management.api;

import com.collegefest.management.entity.Participant;
import com.collegefest.management.service.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantApiController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantApiController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping
    public List<Participant> getAll() {
        return participantService.getAllParticipants();
    }

    @GetMapping("/{id}")
    public Participant getOne(@PathVariable Long id) {
        return participantService.getParticipantById(id);
    }

    /** Pass the target team as a query parameter, e.g. POST /api/participants?teamId=1 */
    @PostMapping
    public ResponseEntity<Participant> create(@Valid @RequestBody Participant participant,
                                               @RequestParam Long teamId) {
        Participant created = participantService.addParticipant(participant, teamId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public Participant update(@PathVariable Long id, @Valid @RequestBody Participant participant) {
        return participantService.updateParticipant(id, participant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }
}
