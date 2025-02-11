package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Challenge;
import org.example.DAO.Entities.Question;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.ChallengeRepository;
import org.example.DAO.Repositories.QuestionRepository;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ChallengeService implements ChallengeIService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> generateQuestions(int difficultyLevel) {
        List<Question> questions = questionRepository.findByDifficultyLevel(difficultyLevel);
        Random random = new Random();
        while (questions.size() > 5) {
            questions.remove(random.nextInt(questions.size()));
        }
        return questions;
    }
    public int evaluateChallenge(List<Question> questions, List<String> userAnswers, int userId) {
        int score = 0;
        // Loop through each question and compare with user's answer
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getCorrectAnswer().equals(userAnswers.get(i))) {
                score += questions.get(i).getDifficultyLevel() * 10; // Score based on difficulty
            }
        }
        // Optionally, you can store the score for the user in the database
        // For example, you can use a UserScore entity or update an existing user's score in your database
        updateUserScore(userId, score);
        return score; // Return the calculated score
    }

    public void updateUserScore(int userId, int earnedScore) {
        // Assuming you have a UserScore or User entity where you store user scores
        // Fetch the user from the database using userId and update their score
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // Set the updated score
        user.setScore_u(user.getScore_u() + earnedScore);
        // Save the user with the updated score
        userRepository.save(user);
    }






}
