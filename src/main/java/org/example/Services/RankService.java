package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.RankType;
import org.example.DAO.Entities.Department;
import org.example.DAO.Entities.Rank;
import org.example.DAO.Entities.Team;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.DepartmentRepository;
import org.example.DAO.Repositories.RankRepository;
import org.example.DAO.Repositories.TeamRepository;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class RankService implements RankIService{
    @Autowired
    RankRepository rankRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @Transactional
    public Rank updateUserRank(int userId) {
        // Fetch the user by ID
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        User user = userOpt.get();
        int userScore = user.getScore_u(); // Fetch the score from the User entity

        // Fetch or create a new Rank entity for the user
        Rank rank = rankRepository.findByUser(user).orElse(new Rank());
        rank.setUser(user);
        rank.setRankType(RankType.INDIVIDUAL); // Set the rank type
        rank.setScore(userScore); // Set the score from the User entity
        rank.setDate(LocalDate.now()); // Set the current date

        // Save the Rank entity
        return rankRepository.save(rank);
    }

    @Transactional
    public Rank updateTeamRank(int teamId) {
        // Fetch the team by ID
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        int teamScore = team.getScore_t(); // Fetch the score from the Team entity

        // Fetch or create a Rank entity for the team
        Rank rank = rankRepository.findByTeam(team).orElse(new Rank());
        rank.setTeam(team);
        rank.setRankType(RankType.TEAM);
        rank.setScore(teamScore); // Set the score from Team entity
        rank.setDate(LocalDate.now()); // Set the current date

        // Save the updated Rank entity
        return rankRepository.save(rank);
    }

    @Transactional
    public Rank updateDepartmentRank(int departmentId) {
        // Fetch the department by ID
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));

        int departmentScore = department.getScore_d(); // Fetch the score from the Department entity

        // Fetch or create a Rank entity for the department
        Rank rank = rankRepository.findByDepartment(department).orElse(new Rank());
        rank.setDepartment(department);
        rank.setRankType(RankType.DEPARTMENT);
        rank.setScore(departmentScore); // Set the score from Department entity
        rank.setDate(LocalDate.now()); // Set the current date

        // Save the updated Rank entity
        return rankRepository.save(rank);
    }

    @Transactional
    public List<Rank> getUserRanks() {
        return rankRepository.findByRankType(RankType.INDIVIDUAL);
    }

    @Transactional
    public List<Rank> getTeamRanks() {
        return rankRepository.findByRankType(RankType.TEAM);
    }
    @Transactional
    public void deleteRank(int rankId) {
        if (!rankRepository.existsById(rankId)) {
            throw new RuntimeException("Rank not found with ID: " + rankId);
        }
        rankRepository.deleteById(rankId);
    }
}
