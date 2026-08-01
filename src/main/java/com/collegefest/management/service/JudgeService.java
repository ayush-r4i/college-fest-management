package com.collegefest.management.service;

import com.collegefest.management.entity.Judge;
import com.collegefest.management.exception.DuplicateResourceException;
import com.collegefest.management.exception.ResourceNotFoundException;
import com.collegefest.management.repository.JudgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JudgeService {

    private final JudgeRepository judgeRepository;

    @Autowired
    public JudgeService(JudgeRepository judgeRepository) {
        this.judgeRepository = judgeRepository;
    }

    public List<Judge> getAllJudges() {
        return judgeRepository.findAll();
    }

    public Judge getJudgeById(Long id) {
        return judgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Judge not found with id: " + id));
    }

    @Transactional
    public Judge createJudge(Judge judge) {
        if (judgeRepository.existsByEmailIgnoreCase(judge.getEmail())) {
            throw new DuplicateResourceException("A judge with email " + judge.getEmail() + " already exists");
        }
        return judgeRepository.save(judge);
    }

    @Transactional
    public Judge updateJudge(Long id, Judge updated) {
        Judge existing = getJudgeById(id);

        judgeRepository.findByEmailIgnoreCase(updated.getEmail()).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                throw new DuplicateResourceException("A judge with email " + updated.getEmail() + " already exists");
            }
        });

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setExpertise(updated.getExpertise());
        return judgeRepository.save(existing);
    }

    @Transactional
    public void deleteJudge(Long id) {
        Judge existing = getJudgeById(id);
        judgeRepository.delete(existing);
    }
}
