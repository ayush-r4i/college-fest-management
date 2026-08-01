package com.collegefest.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "score", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"team_id", "judge_id"})
})
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id", nullable = false)
    private Judge judge;

    @NotNull
    @DecimalMin(value = "0.0", message = "Score cannot be negative")
    @DecimalMax(value = "10.0", message = "Score cannot be more than 10")
    @Column(nullable = false)
    private Double innovation;

    @NotNull
    @DecimalMin(value = "0.0", message = "Score cannot be negative")
    @DecimalMax(value = "10.0", message = "Score cannot be more than 10")
    @Column(name = "technical_implementation", nullable = false)
    private Double technicalImplementation;

    @NotNull
    @DecimalMin(value = "0.0", message = "Score cannot be negative")
    @DecimalMax(value = "10.0", message = "Score cannot be more than 10")
    @Column(nullable = false)
    private Double presentation;

    @NotNull
    @DecimalMin(value = "0.0", message = "Score cannot be negative")
    @DecimalMax(value = "10.0", message = "Score cannot be more than 10")
    @Column(name = "ui_ux", nullable = false)
    private Double uiUx;

    @NotNull
    @DecimalMin(value = "0.0", message = "Score cannot be negative")
    @DecimalMax(value = "10.0", message = "Score cannot be more than 10")
    @Column(name = "overall_impression", nullable = false)
    private Double overallImpression;

    // Automatically calculated: average of the five criteria above
    @Column(name = "final_score")
    private Double finalScore;

    @Column(name = "scored_at")
    private LocalDateTime scoredAt;

    public Score() {
    }

    @PrePersist
    protected void onCreate() {
        this.scoredAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Judge getJudge() {
        return judge;
    }

    public void setJudge(Judge judge) {
        this.judge = judge;
    }

    public Double getInnovation() {
        return innovation;
    }

    public void setInnovation(Double innovation) {
        this.innovation = innovation;
    }

    public Double getTechnicalImplementation() {
        return technicalImplementation;
    }

    public void setTechnicalImplementation(Double technicalImplementation) {
        this.technicalImplementation = technicalImplementation;
    }

    public Double getPresentation() {
        return presentation;
    }

    public void setPresentation(Double presentation) {
        this.presentation = presentation;
    }

    public Double getUiUx() {
        return uiUx;
    }

    public void setUiUx(Double uiUx) {
        this.uiUx = uiUx;
    }

    public Double getOverallImpression() {
        return overallImpression;
    }

    public void setOverallImpression(Double overallImpression) {
        this.overallImpression = overallImpression;
    }

    public Double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    public LocalDateTime getScoredAt() {
        return scoredAt;
    }

    public void setScoredAt(LocalDateTime scoredAt) {
        this.scoredAt = scoredAt;
    }
}
