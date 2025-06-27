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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String questionText;
    String option1;
    String option2;
    String option3;
    String correctAnswer;
    int difficultyLevel;


}
