package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Defi;
import org.example.DAO.Entities.Submission;
import org.example.DAO.Entities.Team;
import org.example.DAO.Repositories.SubmissionRepository;
import org.example.DAO.Repositories.TeamRepository;
import org.example.Services.DefiIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.transaction.Transactional;
import java.util.List;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Defi")
@CrossOrigin(origins = "http://localhost:4200")
public class DefiRestController {
    DefiIService defiIService;
    TeamRepository teamRepository;
    SubmissionRepository submissionRepository;

    @PostMapping("/create")
    public ResponseEntity<Defi> createDefi(@RequestBody Defi defi) {
        Defi createdDefi = defiIService.createDefi(defi);
        return ResponseEntity.ok(createdDefi);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Defi>> getAllDefis() {
        List<Defi> defis = defiIService.getAllDefis();
        return ResponseEntity.ok(defis);
    }

    @GetMapping("/last")
    public ResponseEntity<Defi> getLastCreatedDefi() {
        Defi lastDefi = defiIService.getLastCreatedDefi();
        return ResponseEntity.ok(lastDefi);
    }
    @PostMapping("/submit-to-latest")
    public ResponseEntity<Submission> submitToLatestDefi(
            @RequestParam int teamId,
            @RequestParam String content) {
        Submission submission = defiIService.submitToLatestDefi(teamId, content);
        return ResponseEntity.ok(submission);
    }
    @GetMapping("/check-submission")
    public ResponseEntity<Boolean> checkSubmission(@RequestParam int teamId, @RequestParam int defiId) {
        boolean submitted = defiIService.hasTeamSubmittedToDefi(teamId, defiId);
        return ResponseEntity.ok(submitted);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Defi>> searchDefis(@RequestParam String keyword) {
        return ResponseEntity.ok(defiIService.searchDefisByKeyword(keyword));
    }
    @GetMapping("/submissions/{defiId}")
    public ResponseEntity<List<Submission>> getSubmissionsByDefi(@PathVariable int defiId) {
        return ResponseEntity.ok(defiIService.getSubmissionsByDefiId(defiId));
    }
    @Transactional
    @PutMapping("/score")  // Changed to match frontend call
    public ResponseEntity<Team> addScoreToTeam(
            @RequestParam int teamId,  // Changed to match frontend
            @RequestParam int score) {

        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        team.setScore_t(team.getScore_t() + score);
        teamRepository.save(team);
        return ResponseEntity.ok(team);
    }

}
