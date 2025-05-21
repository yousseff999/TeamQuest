package org.example.DAO.Repositories;

import org.example.DAO.ENUM.TypeEvent;
import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findByEventType(TypeEvent eventType);
    void deleteById(Integer eventId);
    @Query("SELECT COUNT(DISTINCT u.id) FROM Event e JOIN e.participants u")
    long countDistinctParticipants();

}
