package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Repositories.EventRepository;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class EventService implements EventIService{
    EventRepository eventRepository;
}
