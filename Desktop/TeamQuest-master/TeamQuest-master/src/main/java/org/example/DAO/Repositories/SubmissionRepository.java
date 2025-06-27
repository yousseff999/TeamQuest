package org.example.DAO.Repositories;

import org.example.DAO.Entities.Submission;
import org.example.DAO.Entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission,Integer> {
    boolean existsByTeamIdAndDefiId(int teamId, int defiId);
    @Query("SELECT s FROM Submission s JOIN FETCH s.team WHERE s.defi.id = :defiId")
    List<Submission> findByDefiIdWithTeam(@Param("defiId") int defiId);

}
