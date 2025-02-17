package org.example.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.DAO.ENUM.TypeEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int eventId;
     String eventName;
     String description;
     LocalDate startDate;
     LocalDate endDate;
     String location;
     String eventImage;
    @Enumerated(EnumType.STRING)
    TypeEvent eventType;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    List<Feedback> feedbacks;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
     List<User> participants;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    List<Activity> activities;
}
