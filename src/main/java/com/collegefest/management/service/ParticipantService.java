package com.collegefest.management.service;

import com.collegefest.management.entity.Participant;
import com.collegefest.management.entity.Team;
import com.collegefest.management.exception.ResourceNotFoundException;
import com.collegefest.management.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final TeamService teamService;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository, TeamService teamService) {
        this.participantRepository = participantRepository;
        this.teamService = teamService;
    }

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public Participant getParticipantById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
    }

    public List<Participant> getParticipantsByTeam(Long teamId) {
        return participantRepository.findByTeamId(teamId);
    }

    @Transactional
    public Participant addParticipant(Participant participant, Long teamId) {
        Team team = teamService.getTeamById(teamId);

        // Business rule: cannot exceed the event's max team size
        teamService.assertCanAddParticipant(team);

        participant.setTeam(team);
        return participantRepository.save(participant);
    }

    @Transactional
    public Participant updateParticipant(Long id, Participant updated) {
        Participant existing = getParticipantById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setCollege(updated.getCollege());
        return participantRepository.save(existing);
    }

    @Transactional
    public void deleteParticipant(Long id) {
        Participant existing = getParticipantById(id);
        participantRepository.delete(existing);
    }
}
