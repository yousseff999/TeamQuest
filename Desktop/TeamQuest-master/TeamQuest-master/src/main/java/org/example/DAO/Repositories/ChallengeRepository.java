package org.example.DAO.Repositories;

import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge,Integer> {
    List<Challenge> findByCreatorIdOrOpponentId(int creatorId, int opponentId);
    @Query("SELECT c.difficultyLevel FROM Challenge c WHERE c.id = :id")
    Integer findDifficultyLevelById(@Param("id") int id);


}
