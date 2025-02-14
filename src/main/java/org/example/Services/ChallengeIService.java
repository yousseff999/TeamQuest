package org.example.Services;

import org.example.DAO.ENUM.RankType;
import org.example.DAO.Entities.Challenge;
import org.example.DAO.Entities.Question;

import java.util.List;

public interface ChallengeIService {
    public Challenge createChallenge(Challenge challenge);
    public List<Challenge> getAllChallenges();
    public Challenge updateChallenge(int id, Challenge challengeDetails);
    public void deleteChallenge(int id);
    public List<Question> generateQuestions(int difficultyLevel);
    public int evaluateChallenge(List<Question> questions, List<String> userAnswers,Integer userId, Integer teamId, Integer departmentId);
    public void updateUserScore(int userId, int score);
    public void updateTeamScore(int teamId, int earnedScore);
    public void updateDepartmentScore(int departmentId, int earnedScore);
}
