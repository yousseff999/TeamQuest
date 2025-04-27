package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.RankType;
import org.example.DAO.Entities.Challenge;
import org.example.DAO.Entities.ChatMessage;
import org.example.DAO.Entities.Question;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.UserRepository;
import org.example.Services.ChallengeIService;
import org.example.Services.RankIService;
import org.example.Services.RankService;
import org.example.Services.UserIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Challenge")
@CrossOrigin(origins = "http://localhost:4200")
public class ChallengeRestController {
    ChallengeIService challengeIService;
    UserRepository userRepository;
    RankService rankService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @PostMapping("/create")
    public ResponseEntity<Challenge> createChallenge(
            @RequestBody Challenge challenge,
            @RequestParam Integer creatorId,
            @RequestParam Integer opponentId
    ) {
        Challenge created = challengeIService.createChallenge(challenge, creatorId, opponentId);
        ChatMessage notification = new ChatMessage();
        notification.setSender(created.getOpponent().getUsername()); // Make sure Challenge has opponent with username
        notification.setContent("You have been challenged by " + created.getCreator().getUsername());

        // Send the notification to the opponent via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + created.getOpponent().getUsername(), notification);
        return ResponseEntity.ok(created);
    }
    @GetMapping("/user-challenges")
    public ResponseEntity<List<Challenge>> getChallengesByUser(
            @RequestParam int creatorId,
            @RequestParam int opponentId
    ) {
        List<Challenge> challenges = challengeIService.getChallengesByUser(creatorId, opponentId);
        return ResponseEntity.ok(challenges);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Challenge> updateChallenge(@PathVariable int id, @RequestBody Challenge challengeDetails) {
        Challenge updatedChallenge = challengeIService.updateChallenge(id, challengeDetails);
        return ResponseEntity.ok(updatedChallenge);
    }

    @GetMapping("/getall")
    public List<Challenge> getAllChallenges() {
        return challengeIService.getAllChallenges();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteChallenge(@PathVariable int id) {
        challengeIService.deleteChallenge(id);
    }

    @GetMapping("/get_questions")
    public List<Question> generateQuestions(@RequestParam int difficultyLevel) {
        return challengeIService.generateQuestions(difficultyLevel);
    }

    @PostMapping("/evaluate")
    public Map<String, Integer> evaluateChallenge(@RequestBody Map<String, Object> payload) {
        List<Question> questions = new ArrayList<>();
        List<String> userAnswers = new ArrayList<>();
        Integer userId = null;
        Integer teamId = null;
        Integer departmentId = null;

        if (payload.get("questions") != null) {
            List<Map<String, Object>> questionsList = (List<Map<String, Object>>) payload.get("questions");
            for (Map<String, Object> q : questionsList) {
                Question question = new Question();
                question.setQuestionText((String) q.get("questionText"));
                question.setCorrectAnswer((String) q.get("correctAnswer"));
                question.setDifficultyLevel((Integer) q.get("difficultyLevel"));
                questions.add(question);
            }
        }

        if (payload.get("userAnswers") != null) {
            userAnswers = (List<String>) payload.get("userAnswers");
        }

        if (payload.get("userId") != null) {
            userId = (Integer) payload.get("userId");
        }
        if (payload.get("teamId") != null) {
            teamId = (Integer) payload.get("teamId");
        }
        if (payload.get("departmentId") != null) {
            departmentId = (Integer) payload.get("departmentId");
        }

        int score = challengeIService.evaluateChallenge(questions, userAnswers, userId, teamId, departmentId);

        Map<String, Integer> response = new HashMap<>();
        response.put("score", score);

        return response;
    }

    @PutMapping("/updateScore")
    public ResponseEntity<String> updateScore(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer teamId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam int score) {

        try {
            if (userId != null) {
                challengeIService.updateUserScore(userId, score);
                return ResponseEntity.ok("User score updated successfully");
            } else if (teamId != null) {
                challengeIService.updateTeamScore(teamId, score);
                return ResponseEntity.ok("Team score updated successfully");
            } else if (departmentId != null) {
                challengeIService.updateDepartmentScore(departmentId, score);
                return ResponseEntity.ok("Department score updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Please provide a valid ID (userId, teamId, or departmentId)");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
        }
    }



}
