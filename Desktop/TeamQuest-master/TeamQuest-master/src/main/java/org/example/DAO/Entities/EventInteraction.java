package org.example.DAO.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.DAO.ENUM.InteractionType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @Enumerated(EnumType.STRING)
    InteractionType interactionType;

    LocalDateTime timestamp;
}

