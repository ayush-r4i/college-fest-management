package com.collegefest.management.service;

import com.collegefest.management.entity.Event;
import com.collegefest.management.entity.Participant;
import com.collegefest.management.entity.Team;
import com.collegefest.management.exception.DuplicateResourceException;
import com.collegefest.management.repository.ParticipantRepository;
import com.collegefest.management.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamRegistrationFlowTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private EventService eventService;

    @Mock
    private ParticipantRepository participantRepository;

    private TeamService teamService;
    private ParticipantService participantService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        teamService = new TeamService(teamRepository, eventService);
        participantService = new ParticipantService(participantRepository, teamService);

        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setName("Hackathon 2026");
        testEvent.setMaxTeamSize(4);
    }

    @Test
    void testRegisterTeam_automaticallyAddsLeaderAsParticipantOne() {
        when(eventService.getEventById(1L)).thenReturn(testEvent);
        when(teamRepository.existsByTeamNameIgnoreCaseAndEventId("CodeWizards", 1L)).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Team team = new Team();
        team.setTeamName("CodeWizards");
        team.setLeaderName("Alice Smith");
        team.setLeaderEmail("alice@example.com");
        team.setLeaderPhone("555-1234");

        Team savedTeam = teamService.registerTeam(team, 1L);

        assertNotNull(savedTeam);
        assertNotNull(savedTeam.getParticipants());
        assertEquals(1, savedTeam.getParticipants().size(), "Leader should be added as participant #1");

        Participant leaderPart = savedTeam.getParticipants().get(0);
        assertEquals("Alice Smith", leaderPart.getName());
        assertEquals("alice@example.com", leaderPart.getEmail());
        assertEquals("555-1234", leaderPart.getPhone());
        assertEquals(team, leaderPart.getTeam());
    }

    @Test
    void testAddParticipant_preventsDuplicateLeader() {
        Team team = new Team();
        team.setId(10L);
        team.setTeamName("CodeWizards");
        team.setLeaderName("Alice Smith");
        team.setLeaderEmail("alice@example.com");
        team.setEvent(testEvent);

        Participant leader = new Participant();
        leader.setId(101L);
        leader.setName("Alice Smith");
        leader.setEmail("alice@example.com");
        leader.setTeam(team);
        team.setParticipants(new ArrayList<>());
        team.getParticipants().add(leader);

        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        // Attempt to add Alice Smith again
        Participant duplicateParticipant = new Participant();
        duplicateParticipant.setName("Alice Smith");
        duplicateParticipant.setEmail("alice@example.com");

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class, () -> {
            participantService.addParticipant(duplicateParticipant, 10L);
        });

        assertTrue(ex.getMessage().contains("Team leader"));
        verify(participantRepository, never()).save(any(Participant.class));
    }

    @Test
    void testAddParticipant_allowsNormalMember() {
        Team team = new Team();
        team.setId(10L);
        team.setTeamName("CodeWizards");
        team.setLeaderName("Alice Smith");
        team.setEvent(testEvent);

        Participant leader = new Participant();
        leader.setId(101L);
        leader.setName("Alice Smith");
        leader.setTeam(team);
        team.setParticipants(new ArrayList<>());
        team.getParticipants().add(leader);

        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(participantRepository.save(any(Participant.class))).thenAnswer(invocation -> {
            Participant p = invocation.getArgument(0);
            p.setId(102L);
            return p;
        });

        Participant member2 = new Participant();
        member2.setName("Bob Jones");
        member2.setEmail("bob@example.com");

        Participant saved = participantService.addParticipant(member2, 10L);

        assertNotNull(saved);
        assertEquals("Bob Jones", saved.getName());
        assertEquals(team, saved.getTeam());
        verify(participantRepository, times(1)).save(member2);
    }
}
