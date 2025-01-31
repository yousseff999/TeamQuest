package org.example.DAO.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.DAO.ENUM.Role;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
         int id;
         String username;
         String email;
         String password;
         int totalScore;
        @Enumerated(EnumType.STRING)
        Role role;
      @ManyToMany(mappedBy = "participants")
      List<Event> events;
      @ManyToOne
      @JoinColumn(name = "team_id")
      Team team;


}