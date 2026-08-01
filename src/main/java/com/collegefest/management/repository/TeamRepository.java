package com.collegefest.management.repository;

import com.collegefest.management.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByEventId(Long eventId);
    boolean existsByTeamNameIgnoreCaseAndEventId(String teamName, Long eventId);
}
