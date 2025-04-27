package org.example.DAO.Repositories;

import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team,Integer> {
    Optional<Team> findByName(String name);
    // Check if a team with the given name exists
    boolean existsByName(String name);


}
