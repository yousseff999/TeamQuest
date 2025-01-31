package org.example.DAO.Repositories;

import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge,Integer> {
}
