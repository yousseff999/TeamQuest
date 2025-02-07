package org.example.Services;

import org.example.DAO.Entities.Rank;

import java.util.List;

public interface RankIService {
    public Rank updateUserRank(int userId);
    public Rank updateTeamRank(int teamId);
    public List<Rank> getUserRanks();
    public List<Rank> getTeamRanks();
    public void deleteRank(int rankId);
    public Rank updateDepartmentRank(int departmentId);
}
