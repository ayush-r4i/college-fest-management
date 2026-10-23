package com.collegefest.management.repository;

import com.collegefest.management.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByTeamId(Long teamId);
    List<Score> findByJudgeId(Long judgeId);
    Optional<Score> findByTeamIdAndJudgeId(Long teamId, Long judgeId);

    @Query("SELECT s FROM Score s WHERE s.team.event.id = :eventId")
    List<Score> findByTeamEventId(@Param("eventId") Long eventId);
}
