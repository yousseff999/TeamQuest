package org.example.DAO.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String description;
    LocalDate awardedDate;

    @ManyToOne
    @JoinColumn(name = "team_id")
    Team team;

    @PrePersist
    protected void onCreate() {
        awardedDate = LocalDate.now();
    }
}
