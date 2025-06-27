package org.example.DAO.Repositories;

import org.example.DAO.Entities.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

    List<GiftCard> findByIsActiveTrueOrderByRequiredScoreAsc();

    @Query("SELECT gc FROM GiftCard gc WHERE gc.isActive = true AND gc.requiredScore <= :userScore ORDER BY gc.requiredScore ASC")
    List<GiftCard> findAffordableGiftCards(Integer userScore);
}
