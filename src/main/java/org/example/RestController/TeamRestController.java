package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Team;
import org.example.DAO.Repositories.TeamRepository;
import org.example.Services.TeamIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Team")
@CrossOrigin(origins = "http://localhost:4200")
public class TeamRestController {
    TeamIService teamIService;
    TeamRepository teamRepository;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Team createTeam(@RequestBody Team team) {
        return teamIService.createTeam(team);
    }
    @GetMapping("/getteam/{id}")
    public Team getTeamById(@PathVariable int id) {
        return teamIService.getTeamById(id);
    }
    @GetMapping("/getall")
    public List<Team> getAllTeams() {
        return teamIService.getAllTeams();
    }

    @PutMapping("/updateteam/{id}")
    public Team updateTeam(@PathVariable int id, @RequestBody Team team) {
        return teamIService.updateTeam(id, team);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTeam(@PathVariable int id) {
        teamIService.deleteTeam(id);
    }

    @PostMapping("/{teamId}/add-user/{userId}")
    public Team addUserToTeam(@PathVariable int teamId, @PathVariable int userId) {
        return teamIService.addUserToTeam(teamId, userId);
    }

    @PostMapping("/{teamId}/remove-user/{userId}")
    public Team removeUserFromTeam(@PathVariable int teamId, @PathVariable int userId) {
        return teamIService.removeUserFromTeam(teamId, userId);
    }


    @GetMapping("/scores")
    public List<Object[]> getTeamNamesAndScores() {
        return teamIService.getAllTeamNamesAndScoresOrdered();
    }
    @GetMapping("/teamuser/{userId}")
    public ResponseEntity<Team> getTeamByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(teamIService.getTeamByUserId(userId));
    }
    @GetMapping("/count")
    public long getTeamCount() {
        return teamIService.countAllTeams();
    }
    @GetMapping("/weekly-percentage-change")
    public double getWeeklyPercentageChange() {
        return teamIService.getWeeklyTeamsCreationPercentageChange();
    }
}
