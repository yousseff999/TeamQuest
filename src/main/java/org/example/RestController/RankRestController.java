package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.RankType;
import org.example.DAO.Entities.Rank;
import org.example.Services.RankIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Rank")
@CrossOrigin(origins = "http://localhost:4200")
public class RankRestController {
    RankIService rankIService;

    @PutMapping("/update/user/{userId}")
    public ResponseEntity<Rank> updateUserRank(@PathVariable int userId) {
        Rank updatedRank = rankIService.updateUserRank(userId);
        return ResponseEntity.ok(updatedRank);
    }

    @PutMapping("/update/team/{teamId}")
    public ResponseEntity<Rank> updateTeamRank(@PathVariable int teamId) {
        Rank updatedRank = rankIService.updateTeamRank(teamId);
        return ResponseEntity.ok(updatedRank);
    }

    @PutMapping("/updateDepartment/{departmentId}")
    public ResponseEntity<Rank> updateDepartmentRank(@PathVariable int departmentId) {
        Rank updatedRank = rankIService.updateDepartmentRank(departmentId);
        return ResponseEntity.ok(updatedRank);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Rank>> getUserRanks() {
        return ResponseEntity.ok(rankIService.getUserRanks());
    }

    @GetMapping("/teams")
    public ResponseEntity<List<Rank>> getTeamRanks() {
        return ResponseEntity.ok(rankIService.getTeamRanks());
    }

    @DeleteMapping("/delete/{rankId}")
    public ResponseEntity<String> deleteRank(@PathVariable int rankId) {
        rankIService.deleteRank(rankId);
        return ResponseEntity.ok("Rank deleted successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateRank(@RequestParam Integer entityId, @RequestParam RankType rankType) {
        // Update the rank based on userId, teamId, or departmentId and rankType
        rankIService.updateRank(entityId, rankType);
        return ResponseEntity.ok("Rank updated successfully.");
    }
}
