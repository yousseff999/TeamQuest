package org.example.Services;

import org.example.DAO.Entities.Defi;
import org.example.DAO.Entities.Submission;

import java.util.List;

public interface DefiIService {
    public Defi createDefi(Defi defi);
    public List<Defi> getAllDefis();
    public Defi getLastCreatedDefi();
    public Submission submitToLatestDefi(int teamId, String content);
    public boolean hasTeamSubmittedToDefi(int teamId, int defiId);
    List<Defi> searchDefisByKeyword(String keyword);
    public List<Submission> getSubmissionsByDefiId(int defiId);
}
