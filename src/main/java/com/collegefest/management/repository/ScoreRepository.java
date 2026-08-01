package com.collegefest.management.repository;

import com.collegefest.management.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByTeamId(Long teamId);
    List<Score> findByJudgeId(Long judgeId);
    Optional<Score> findByTeamIdAndJudgeId(Long teamId, Long judgeId);
}
