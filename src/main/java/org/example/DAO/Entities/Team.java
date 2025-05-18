package org.example.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int id;
     String name;
     String description;
     LocalDate creationDate;
     int score_t;

    @OneToMany(mappedBy = "team")
    @JsonIgnoreProperties("team")
    List<Submission> submissions;
    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<User> members;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rank> ranks = new ArrayList<>();
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Badge> badges = new ArrayList<>();

    // Automatically set the creation date when the team is created
    @PrePersist
    protected void onCreate() {
        creationDate = LocalDate.now();
    }

}
