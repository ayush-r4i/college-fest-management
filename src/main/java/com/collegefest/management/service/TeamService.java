package com.collegefest.management.service;

import com.collegefest.management.entity.Event;
import com.collegefest.management.entity.Participant;
import com.collegefest.management.entity.Team;
import com.collegefest.management.exception.DuplicateResourceException;
import com.collegefest.management.exception.ResourceNotFoundException;
import com.collegefest.management.exception.TeamCapacityExceededException;
import com.collegefest.management.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final EventService eventService;

    @Autowired
    public TeamService(TeamRepository teamRepository, EventService eventService) {
        this.teamRepository = teamRepository;
        this.eventService = eventService;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }

    public List<Team> getTeamsByEvent(Long eventId) {
        return teamRepository.findByEventId(eventId);
    }

    @Transactional
    public Team registerTeam(Team team, Long eventId) {
        Event event = eventService.getEventById(eventId);

        if (teamRepository.existsByTeamNameIgnoreCaseAndEventId(team.getTeamName(), eventId)) {
            throw new DuplicateResourceException(
                    "A team named \"" + team.getTeamName() + "\" is already registered for this event");
        }

        team.setEvent(event);

        if (team.getParticipants() == null) {
            team.setParticipants(new ArrayList<>());
        }

        // Automatically add the team leader as Participant #1 if not already present
        boolean leaderAlreadyPresent = team.getParticipants().stream()
                .anyMatch(p -> p.getName() != null && p.getName().trim().equalsIgnoreCase(team.getLeaderName().trim()));

        if (!leaderAlreadyPresent) {
            Participant leader = new Participant();
            leader.setName(team.getLeaderName().trim());
            leader.setEmail(team.getLeaderEmail());
            leader.setPhone(team.getLeaderPhone());
            leader.setTeam(team);
            team.getParticipants().add(0, leader);
        }

        // Check that initial participants list (including the leader) does not exceed event limits
        int totalParticipants = team.getParticipants().size();
        if (totalParticipants > event.getMaxTeamSize()) {
            throw new TeamCapacityExceededException(
                    "Team \"" + team.getTeamName() + "\" has " + totalParticipants +
                            " members, which exceeds the limit of " + event.getMaxTeamSize() +
                            " for event \"" + event.getName() + "\"");
        }

        team.getParticipants().forEach(p -> p.setTeam(team));
        return teamRepository.save(team);
    }

    @Transactional
    public Team updateTeam(Long id, Team updated) {
        Team existing = getTeamById(id);
        String oldLeaderName = existing.getLeaderName();
        String oldLeaderEmail = existing.getLeaderEmail();

        existing.setTeamName(updated.getTeamName());
        existing.setLeaderName(updated.getLeaderName());
        existing.setLeaderEmail(updated.getLeaderEmail());
        existing.setLeaderPhone(updated.getLeaderPhone());

        // Keep leader participant record in sync if present
        if (existing.getParticipants() != null && !existing.getParticipants().isEmpty()) {
            for (Participant p : existing.getParticipants()) {
                if ((oldLeaderName != null && oldLeaderName.trim().equalsIgnoreCase(p.getName() != null ? p.getName().trim() : ""))
                        || (oldLeaderEmail != null && !oldLeaderEmail.trim().isEmpty() && oldLeaderEmail.trim().equalsIgnoreCase(p.getEmail() != null ? p.getEmail().trim() : ""))) {
                    p.setName(updated.getLeaderName());
                    p.setEmail(updated.getLeaderEmail());
                    p.setPhone(updated.getLeaderPhone());
                    break;
                }
            }
        }

        return teamRepository.save(existing);
    }

    @Transactional
    public void deleteTeam(Long id) {
        Team existing = getTeamById(id);
        teamRepository.delete(existing);
    }

    /** Used by ParticipantService to enforce the max-team-size rule when adding members one by one. */
    public void assertCanAddParticipant(Team team) {
        int currentSize = team.getParticipants() == null ? 0 : team.getParticipants().size();
        int maxSize = team.getEvent().getMaxTeamSize();
        if (currentSize >= maxSize) {
            throw new TeamCapacityExceededException(
                    "Team \"" + team.getTeamName() + "\" already has the maximum of " +
                            maxSize + " members allowed for event \"" + team.getEvent().getName() + "\"");
        }
    }
}
