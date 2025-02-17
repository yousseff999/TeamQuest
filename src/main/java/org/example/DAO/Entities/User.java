package org.example.DAO.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.DAO.ENUM.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
         int score_u;
        @Enumerated(EnumType.STRING)
        Role role;
      @ManyToMany(mappedBy = "participants")
      List<Event> events;
      @ManyToOne
      @JoinColumn(name = "team_id")
      Team team;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<Rank> ranks = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToMany(mappedBy = "users") // This must match the field in Activity
    private Set<Activity> activities;






}