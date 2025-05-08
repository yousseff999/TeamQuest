package org.example.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
     int score_c;


    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    User creator;

    @ManyToOne
    @JoinColumn(name = "opponent_id", nullable = false)
    User opponent;


}
