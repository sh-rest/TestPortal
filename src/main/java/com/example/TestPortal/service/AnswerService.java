package com.example.TestPortal.service;

import com.example.TestPortal.model.Answer;
import com.example.TestPortal.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void submitAnswer(Answer answer) {
        answerRepository.submitAnswer(answer);
    }

    public List<Answer> getSubmissionAnswers(int submissionId) {
        return answerRepository.getSubmissionAnswers(submissionId);
    }
} 