package org.example.DAO.Repositories;

import org.example.DAO.ENUM.InteractionType;
import org.example.DAO.ENUM.TypeEvent;
import org.example.DAO.Entities.Department;
import org.example.DAO.Entities.Event;
import org.example.DAO.Entities.EventInteraction;
import org.example.DAO.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventInteractionRepository extends JpaRepository<EventInteraction,Integer> {
    List<EventInteraction> findByUserId(int userId);
    List<EventInteraction> findByEventEventId(int eventId);
    List<EventInteraction> findByUserAndEvent(User user, Event event);
    List<EventInteraction> findByEventAndInteractionType(Event event, InteractionType interactionType);
    @Modifying
    @Query("DELETE FROM EventInteraction ei WHERE ei.event.eventId = :eventId")
    void deleteByEventId(@Param("eventId") int eventId);
    @Query("SELECT ei.interactionType, COUNT(ei) FROM EventInteraction ei GROUP BY ei.interactionType")
    List<Object[]> countAllInteractions();

    @Query("SELECT ei.interactionType, COUNT(ei) FROM EventInteraction ei WHERE ei.event.eventType = :type GROUP BY ei.interactionType")
    List<Object[]> countInteractionsByType(@Param("type") TypeEvent type);

}
