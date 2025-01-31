package org.example.DAO.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int id;
     String title;
     String description;
     int difficultyLevel;
     int score;

    @ManyToOne
    @JoinColumn(name = "user_id")
     User user;
}
