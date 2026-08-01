package com.collegefest.management.repository;

import com.collegefest.management.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByTeamId(Long teamId);
}
