package org.example.DAO.Repositories;

import org.example.DAO.Entities.UserGiftCard;
import org.example.DAO.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGiftCardRepository extends JpaRepository<UserGiftCard, Long> {

    List<UserGiftCard> findByUserIdOrderByExchangedAtDesc(Integer userId);

    boolean existsByUserAndGiftCardId(User user, Long giftCardId);
}
