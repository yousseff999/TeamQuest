package org.example.DAO.Repositories;

import org.example.DAO.ENUM.TypeEvent;
import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findByEventType(TypeEvent eventType);
}
