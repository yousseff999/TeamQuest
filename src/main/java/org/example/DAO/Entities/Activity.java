package org.example.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int activityID;
     String activityName;
     String description;
     LocalDate startDate;
     LocalDate endDate;
     int currentParticipants;
     String activityImage;
     int maxParticipants;
     String status;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "event_id")
     Event event;
    @ManyToMany
    @JoinTable(
            name = "activity_participants",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
