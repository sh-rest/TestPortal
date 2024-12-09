package com.example.TestPortal.repository;

import com.example.TestPortal.model.Answer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AnswerRepository {
    private final JdbcTemplate jdbcTemplate;

    public AnswerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void submitAnswer(Answer answer) {
        String sql = "INSERT INTO Answer (submission_id, question_id, selected_option, is_correct) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, answer.getSubmissionId(), answer.getQuestionId(), 
                          answer.getSelectedOption(), answer.isCorrect());
    }

    public List<Answer> getSubmissionAnswers(int submissionId) {
        String sql = "SELECT * FROM Answer WHERE submission_id = ?";
        return jdbcTemplate.query(sql, answerRowMapper(), submissionId);
    }

    private RowMapper<Answer> answerRowMapper() {
        return (rs, _) -> {
            Answer answer = new Answer();
            answer.setAnswerId(rs.getInt("answer_id"));
            answer.setSubmissionId(rs.getInt("submission_id"));
            answer.setQuestionId(rs.getInt("question_id"));
            answer.setSelectedOption(rs.getString("selected_option"));
            answer.setCorrect(rs.getBoolean("is_correct"));
            return answer;
        };
    }
}