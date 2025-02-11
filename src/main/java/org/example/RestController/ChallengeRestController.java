package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Challenge;
import org.example.DAO.Entities.Question;
import org.example.Services.ChallengeIService;
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

    @GetMapping("/get_questions")
    public List<Question> generateQuestions(@RequestParam int difficultyLevel) {
        return challengeIService.generateQuestions(difficultyLevel);
    }

    @PostMapping("/evaluate")
    public int evaluateChallenge(@RequestBody List<Question> questions, @RequestParam List<String> userAnswers,@RequestParam int userId) {
        // Call the service to evaluate the challenge
        return challengeIService.evaluateChallenge(questions, userAnswers, userId);
    }

    @PutMapping("/updateScore")
    public ResponseEntity<String> updateUserScore(
            @RequestParam int userId,  // Fetching userId from query parameters
            @RequestParam int score)   // Fetching score from query parameters
    {
            try {
                // Call the service method to update the user score
                challengeIService.updateUserScore(userId, score);

                // Return a success response
                return ResponseEntity.ok("User score updated successfully");
            } catch (RuntimeException e) {
                // Handle the error if the user is not found or other errors occur
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
    }




}
