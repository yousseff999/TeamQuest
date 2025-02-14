package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.RankType;
import org.example.DAO.Entities.Challenge;
import org.example.DAO.Entities.Question;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.UserRepository;
import org.example.Services.ChallengeIService;
import org.example.Services.RankIService;
import org.example.Services.RankService;
import org.example.Services.UserIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
    @PostMapping("/create")
    public ResponseEntity<Challenge> createChallenge(@RequestBody Map<String, Object> requestBody) {
        // Récupère l'userId du JSON, si absent, il renvoie null
        Integer userId = (Integer) requestBody.get("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(null);  // Retourne une erreur si l'userId est absent
        }

        // Vérifier si l'utilisateur existe dans la base de données
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Créer le challenge avec les autres données
        Challenge challenge = Challenge.builder()
                .title((String) requestBody.get("title"))
                .description((String) requestBody.get("description"))
                .difficultyLevel((Integer) requestBody.get("difficultyLevel"))
                .score_c((Integer) requestBody.get("score_c"))
                .user(user)
                .build();

        // Sauvegarder et retourner le challenge créé
        Challenge savedChallenge = challengeIService.createChallenge(challenge);
        return ResponseEntity.ok(savedChallenge);
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
    public int evaluateChallenge(@RequestBody List<Question> questions, @RequestParam List<String> userAnswers,
                                 @RequestParam(required = false) Integer userId,
                                 @RequestParam(required = false) Integer teamId,
                                 @RequestParam(required = false) Integer departmentId) {
        return challengeIService.evaluateChallenge(questions, userAnswers, userId, teamId, departmentId);
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
