package org.example.DAO.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_gift_cards")
public class UserGiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "gift_card_id", nullable = false)
    GiftCard giftCard;

    @Column(nullable = false)
    Integer scoreUsed;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime exchangedAt;

    String redemptionCode;
}
