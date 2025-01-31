package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Team;
import org.example.DAO.Repositories.TeamRepository;
import org.example.DAO.Repositories.UserRepository;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class TeamService implements TeamIService{
    TeamRepository teamRepository;
    UserRepository userRepository;

    public Team createTeam(Team team) {
        // Ensure the team name is unique
        if (teamRepository.existsByName(team.getName())) {
            throw new RuntimeException("Team name already exists");
        }
        return teamRepository.save(team);
    }

    public Team getTeamById(int id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team updateTeam(int id, Team team) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        existingTeam.setName(team.getName());
        existingTeam.setScore(team.getScore());
        existingTeam.setMembers(team.getMembers());

        return teamRepository.save(existingTeam);
    }
}
