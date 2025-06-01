package org.example.Services;

import org.example.DAO.Entities.GiftCard;
import org.example.DAO.Entities.GiftCardDTO;
import org.example.DAO.Entities.User;
import org.example.DAO.Entities.UserGiftCard;
import org.example.DAO.Repositories.GiftCardRepository;
import org.example.DAO.Repositories.UserGiftCardRepository;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GiftCardService {

    @Autowired
    private GiftCardRepository giftCardRepository;

    @Autowired
    private UserGiftCardRepository userGiftCardRepository;

    @Autowired
    private UserRepository userRepository;

    public List<GiftCardDTO> getAllGiftCards(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GiftCard> giftCards = giftCardRepository.findByIsActiveTrueOrderByRequiredScoreAsc();

        return giftCards.stream().map(gc -> {
            GiftCardDTO dto = convertToDTO(gc);
            dto.setCanAfford(user.getScore_u() >= gc.getRequiredScore());
            dto.setAlreadyOwned(userGiftCardRepository.existsByUserAndGiftCardId(user, gc.getId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public String exchangeGiftCard(Long giftCardId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GiftCard giftCard = giftCardRepository.findById(giftCardId)
                .orElseThrow(() -> new RuntimeException("Gift card not found"));

        if (user.getScore_u() < giftCard.getRequiredScore()) {
            throw new RuntimeException("Insufficient score");
        }

        if (userGiftCardRepository.existsByUserAndGiftCardId(user, giftCardId)) {
            throw new RuntimeException("Gift card already owned");
        }

        // Déduire le score
        user.setScore_u(user.getScore_u() - giftCard.getRequiredScore());
        userRepository.save(user);

        // Créer l'échange
        UserGiftCard userGiftCard = UserGiftCard.builder()
                .user(user)
                .giftCard(giftCard)
                .scoreUsed(giftCard.getRequiredScore())
                .redemptionCode(generateRedemptionCode())
                .build();

        userGiftCardRepository.save(userGiftCard);

        return "Exchange successful";
    }

    private String generateRedemptionCode() {
        return "GC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private GiftCardDTO convertToDTO(GiftCard giftCard) {
        GiftCardDTO dto = new GiftCardDTO();
        dto.setId(giftCard.getId());
        dto.setTitle(giftCard.getTitle());
        dto.setDescription(giftCard.getDescription());
        dto.setRequiredScore(giftCard.getRequiredScore());
        dto.setImageUrl(giftCard.getImageUrl());
        dto.setBrand(giftCard.getBrand());
        dto.setCategory(giftCard.getCategory());
        dto.setCreatedAt(giftCard.getCreatedAt());
        return dto;
    }
}
