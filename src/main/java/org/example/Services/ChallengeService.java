package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.RankType;
import org.example.DAO.Entities.*;
import org.example.DAO.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ChallengeService implements ChallengeIService {
    @Autowired
    private UserRepository userRepository;
    TeamRepository teamRepository;
    DepartmentRepository departmentRepository;
    @Autowired
    private QuestionRepository questionRepository;
    ChallengeRepository challengeRepository;
    @Autowired
    private Environment environment;

    @Override
    public Challenge createChallenge(Challenge challenge, Integer creatorId, Integer opponentId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));
        User opponent = userRepository.findById(opponentId)
                .orElseThrow(() -> new RuntimeException("Opponent not found"));

        challenge.setCreator(creator);
        challenge.setOpponent(opponent);

        return challengeRepository.save(challenge);
    }


    @Override
    public List<Challenge> getChallengesByUser(int creatorId, int opponentId) {
        return challengeRepository.findByCreatorIdOrOpponentId(creatorId, opponentId);
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }
    public Challenge updateChallenge(int id, Challenge challengeDetails) {
        Challenge challenge = challengeRepository.findById(id).orElse(null);

        if (challenge == null) {
            // Gérer le cas où le challenge n'existe pas, soit en retournant null ou autre logique
            return null; // ou un message d'erreur spécifique
        }

        // Mise à jour des propriétés du challenge
        challenge.setTitle(challengeDetails.getTitle());
        challenge.setDescription(challengeDetails.getDescription());
        challenge.setDifficultyLevel(challengeDetails.getDifficultyLevel());
        // Sauvegarder et retourner le challenge mis à jour
        return challengeRepository.save(challenge);
    }
    @Override
    public void deleteChallenge(int id) {
        challengeRepository.deleteById(id);
    }

    public List<Question> generateQuestions(int difficultyLevel) {
        List<Question> questions = questionRepository.findByDifficultyLevel(difficultyLevel);
        Random random = new Random();
        while (questions.size() > 5) {
            questions.remove(random.nextInt(questions.size()));
        }
        return questions;
    }
    public int evaluateChallenge(List<Question> questions, List<String> userAnswers, Integer userId, Integer teamId, Integer departmentId) {
        int score = 0;
        // Loop through each question and compare with user's answer
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getCorrectAnswer().equals(userAnswers.get(i))) {
                score += questions.get(i).getDifficultyLevel() * 10; // Score based on difficulty
            }
        }
        if (userId != null) {
            updateUserScore(userId, score);
        }
        if (teamId != null) {
            updateTeamScore(teamId, score);
        }
        if (departmentId != null) {
            updateDepartmentScore(departmentId, score);
        }
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
    public void updateTeamScore(int teamId, int earnedScore) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setScore_t(team.getScore_t() + earnedScore);
        teamRepository.save(team);
    }
    public void updateDepartmentScore(int departmentId, int earnedScore) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        department.setScore_d(department.getScore_d() + earnedScore);
        departmentRepository.save(department);
    }

    public int getDifficultyLevelByChallengeId(int challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));
        return challenge.getDifficultyLevel();
    }





}
