package com.collegefest.management.service;

import com.collegefest.management.entity.Event;
import com.collegefest.management.entity.Team;
import com.collegefest.management.exception.DuplicateResourceException;
import com.collegefest.management.exception.ResourceNotFoundException;
import com.collegefest.management.exception.TeamCapacityExceededException;
import com.collegefest.management.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // If the team is registered together with an initial list of participants,
        // make sure that list alone does not already exceed the event's member limit.
        int initialParticipants = team.getParticipants() == null ? 0 : team.getParticipants().size();
        if (initialParticipants > event.getMaxTeamSize()) {
            throw new TeamCapacityExceededException(
                    "Team \"" + team.getTeamName() + "\" has " + initialParticipants +
                            " members, which exceeds the limit of " + event.getMaxTeamSize() +
                            " for event \"" + event.getName() + "\"");
        }

        team.setEvent(event);
        if (team.getParticipants() != null) {
            team.getParticipants().forEach(p -> p.setTeam(team));
        }
        return teamRepository.save(team);
    }

    @Transactional
    public Team updateTeam(Long id, Team updated) {
        Team existing = getTeamById(id);
        existing.setTeamName(updated.getTeamName());
        existing.setLeaderName(updated.getLeaderName());
        existing.setLeaderEmail(updated.getLeaderEmail());
        existing.setLeaderPhone(updated.getLeaderPhone());
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
