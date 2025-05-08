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
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idPortfolio;
    String title;
    String description;
    String imagePortfolio;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
