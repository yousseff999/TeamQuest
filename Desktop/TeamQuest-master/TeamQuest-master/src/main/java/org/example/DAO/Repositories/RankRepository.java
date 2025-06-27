package org.example.DAO.Repositories;

import org.example.DAO.ENUM.RankType;
import org.example.DAO.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank,Integer> {
    List<Rank> findByRankTypeOrderByScoreDesc(RankType type);
    Optional<Rank> findByUser(User user);
    Optional<Rank> findByTeam(Team team);
    List<Rank> findByRankType(RankType rankType);
    Optional<Rank> findByDepartment(Department department);
    Optional<Rank> findByRankTypeAndId(RankType rankType, Integer id);
}
