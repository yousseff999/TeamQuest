package org.example.DAO.Repositories;

import org.example.DAO.Entities.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<GiftCard,Integer> {
}
