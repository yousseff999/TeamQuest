package org.example.DAO.Repositories;

import org.example.DAO.ENUM.InteractionType;
import org.example.DAO.Entities.Department;
import org.example.DAO.Entities.Event;
import org.example.DAO.Entities.EventInteraction;
import org.example.DAO.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventInteractionRepository extends JpaRepository<EventInteraction,Integer> {
    List<EventInteraction> findByUserId(int userId);
    List<EventInteraction> findByEventEventId(int eventId);
    List<EventInteraction> findByUserAndEvent(User user, Event event);
    List<EventInteraction> findByEventAndInteractionType(Event event, InteractionType interactionType);

}
