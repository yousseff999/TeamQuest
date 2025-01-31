package org.example.Services;

import org.example.DAO.Entities.Team;

import java.util.List;

public interface TeamIService {
    public Team createTeam(Team team);
    public Team getTeamById(int id);
    public List<Team> getAllTeams();
    public Team updateTeam(int id, Team team);
}
