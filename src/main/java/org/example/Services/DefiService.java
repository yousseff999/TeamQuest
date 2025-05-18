package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Defi;
import org.example.DAO.Entities.Submission;
import org.example.DAO.Entities.Team;
import org.example.DAO.Repositories.DefiRepository;
import org.example.DAO.Repositories.SubmissionRepository;
import org.example.DAO.Repositories.TeamRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class DefiService implements DefiIService{
    DefiRepository defiRepository;
    TeamRepository teamRepository;
    SubmissionRepository submissionRepository;

    @Override
    public Defi createDefi(Defi defi) {
        return defiRepository.save(defi);
    }

    @Override
    public List<Defi> getAllDefis() {
        return defiRepository.findAll();
    }

    @Override
    public Defi getLastCreatedDefi() {
        return defiRepository.findLastCreatedDefi();
    }

    public Submission submitToLatestDefi(int teamId, String content) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        Defi latestDefi = defiRepository.findLastCreatedDefi();
        if (latestDefi == null) {
            throw new IllegalStateException("No defi has been created yet.");
        }

        Submission submission = Submission.builder()
                .team(team)
                .defi(latestDefi)
                .content(content)
                .build();

        return submissionRepository.save(submission);
    }
    public boolean hasTeamSubmittedToDefi(int teamId, int defiId) {
        return submissionRepository.existsByTeamIdAndDefiId(teamId, defiId);
    }
    @Override
    public List<Defi> searchDefisByKeyword(String keyword) {
        return defiRepository.searchDefisByKeyword(keyword);
    }
    @Override
    public List<Submission> getSubmissionsByDefiId(int defiId) {
        return submissionRepository.findByDefiIdWithTeam(defiId);
    }

}
