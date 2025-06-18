package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Team;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.TeamRepository;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class TeamService implements TeamIService{
    TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

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
        existingTeam.setDescription(team.getDescription());
        existingTeam.setCreationDate(team.getCreationDate());
        existingTeam.setScore_t(team.getScore_t());
        existingTeam.setMembers(team.getMembers());

        return teamRepository.save(existingTeam);
    }

    public void deleteTeam(int id) {
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Team not found");
        }
        teamRepository.deleteById(id);
    }

        public Team addUserToTeam(int teamId, int userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setTeam(team); // Set the team for the user
        userRepository.save(user); // Save the updated user
            List<User> teamMembers = userRepository.findByTeamId(teamId)
                    .stream()
                    .filter(u -> u.getId() != userId)
                    .collect(Collectors.toList());

            String subject = "Nouveau membre dans l'équipe " + team.getName();
            String message = "<p>Bonjour,</p><p><strong>" + user.getUsername() +
                    "</strong> a rejoint l'équipe <strong>" + team.getName() + "</strong>.</p>";

            for (User member : teamMembers) {
                if (member.getEmail() != null) {
                    emailService.sendEmail(member.getEmail(), subject, message);
                }
            }
        return teamRepository.save(team); // Save the updated team
    }

    public Team removeUserFromTeam(int teamId, int userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTeam() != null && user.getTeam().getId() == teamId) {
            user.setTeam(null);
            userRepository.save(user);

            List<User> teamMembers = userRepository.findByTeamId(teamId)
                    .stream()
                    .filter(u -> u.getId() != userId)
                    .collect(Collectors.toList());

            String subject = "Un membre a quitté l'équipe " + team.getName();
            String message = "<p>Bonjour,</p><p><strong>" + user.getUsername() +
                    "</strong> a quitté l'équipe <strong>" + team.getName() + "</strong>.</p>";

            for (User member : teamMembers) {
                if (member.getEmail() != null) {
                    emailService.sendEmail(member.getEmail(), subject, message);
                }
            }
        }

        return teamRepository.save(team);
    }

    public List<Object[]> getAllTeamNamesAndScoresOrdered() {
        return teamRepository.findAllTeamNamesAndScoresOrderedByScoreDesc();
    }

    public Team getTeamByUserId(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = user.getTeam();
        if (team == null) {
            throw new RuntimeException("This user is not in any team");
        }

        return team;
    }
    public long countAllTeams() {
        return teamRepository.count();
    }
    public double getWeeklyTeamsCreationPercentageChange() {
        LocalDate now = LocalDate.now();

        // Get start and end of this week (Monday to Sunday)
        LocalDate thisWeekStart = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate thisWeekEnd = thisWeekStart.plusDays(6);

        // Get start and end of previous week
        LocalDate prevWeekStart = thisWeekStart.minusWeeks(1);
        LocalDate prevWeekEnd = thisWeekEnd.minusWeeks(1);

        long thisWeekCount = teamRepository.countTeamsCreatedBetween(thisWeekStart, thisWeekEnd);
        long prevWeekCount = teamRepository.countTeamsCreatedBetween(prevWeekStart, prevWeekEnd);

        if (prevWeekCount == 0) {
            return thisWeekCount > 0 ? 100.0 : 0.0;  // If no teams last week, but some this week, 100%
        }

        // Calculate percentage change
        return ((double)(thisWeekCount - prevWeekCount) / prevWeekCount) * 100;
    }
    public Team getTopScoringTeam() {
        return teamRepository.findTopByOrderByScore_tDesc();
    }

}
