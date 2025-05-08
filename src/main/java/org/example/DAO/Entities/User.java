package org.example.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.DAO.ENUM.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String username;
    String email;
    String password;
    int score_u;
    String resetToken;
    LocalDateTime tokenExpirationTime;

    @Enumerated(EnumType.STRING)
    Role role;

    // ✅ Challenges this user created
    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    List<Challenge> createdChallenges = new ArrayList<>();

    // ✅ Challenges where this user is the opponent
    @OneToMany(mappedBy = "opponent")
    @JsonIgnore
    List<Challenge> receivedChallenges = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    List<Event> events;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "team_id")
    Team team;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Rank> ranks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(mappedBy = "users")
    private Set<Activity> activities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Portfolio> portfolios = new ArrayList<>();
    // UserDetails implementation
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());  // Map Role Enum to authorities
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Allow account to not expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Allow account to be non-locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Allow credentials to not expire
    }

    @Override
    public boolean isEnabled() {
        return true;  // Allow account to be enabled
    }
}
