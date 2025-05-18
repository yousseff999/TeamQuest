package org.example.DAO.Repositories;

import org.example.DAO.Entities.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge,Integer> {
}
