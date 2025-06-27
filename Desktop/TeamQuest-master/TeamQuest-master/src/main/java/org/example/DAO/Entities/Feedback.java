package org.example.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int id;
     String title;
     String description;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "event_id", nullable = false)
     Event event;
    @ManyToOne
    @JoinColumn(name = "user_id")
     User user;
}
