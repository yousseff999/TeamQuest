package org.example.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String content;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime submittedAt;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIgnoreProperties({"submissions", "members", "ranks", "badges"})
    Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defi_id", nullable = false)
    @JsonBackReference
    Defi defi;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
}
