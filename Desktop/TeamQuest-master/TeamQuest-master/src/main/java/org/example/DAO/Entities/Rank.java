package org.example.DAO.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.DAO.ENUM.RankType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int id;
     LocalDate date;
    @Enumerated(EnumType.STRING)
    RankType rankType;
    int score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    Team team;

    @ManyToOne
    @JoinColumn(name = "department_id")  // Add this field
    private Department department;

    @PrePersist
    public void validate() {
        if (user == null && team == null && this.department == null) {
            throw new IllegalStateException("Rank must be linked to a User or Team");
        }
        if (user != null && team != null && department != null) {
            throw new IllegalStateException("Rank cannot be linked to both User and Team");
        }
    }
    public Rank(Integer id, LocalDate date, RankType rankType, int score) {
        this.id = id;
        this.date = date;
        this.rankType = rankType;
        this.score = score;
    }
}
