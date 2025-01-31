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
}
