package org.example.DAO.Repositories;

import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team,Integer> {
    Optional<Team> findByName(String name);
    // Check if a team with the given name exists
    boolean existsByName(String name);

    List<Team> findAll();

    @Query("SELECT t.name, t.score_t FROM Team t ORDER BY t.score_t DESC")
    List<Object[]> findAllTeamNamesAndScoresOrderedByScoreDesc();
    @Query("SELECT COUNT(t) FROM Team t")
    long countAllTeams();
    @Query("SELECT COUNT(t) FROM Team t WHERE t.creationDate BETWEEN :startDate AND :endDate")
    long countTeamsCreatedBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
