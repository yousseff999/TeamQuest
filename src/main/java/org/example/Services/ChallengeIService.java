package org.example.Services;

import org.example.DAO.Entities.Challenge;
import org.example.DAO.Entities.Question;

import java.util.List;

public interface ChallengeIService {
    public List<Question> generateQuestions(int difficultyLevel);
    public int evaluateChallenge(List<Question> questions, List<String> userAnswers,int userId);
    public void updateUserScore(int userId, int score);
}
