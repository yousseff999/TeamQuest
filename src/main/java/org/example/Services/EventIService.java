package org.example.Services;

import org.example.DAO.ENUM.InteractionType;
import org.example.DAO.ENUM.TypeEvent;
import org.example.DAO.Entities.Event;
import org.example.DAO.Entities.EventInteraction;
import org.example.DAO.Entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventIService {
    public Event addEvent(Event event);
    public Event updateEvent(int eventId, Event updatedEvent);
    public void deleteEvent(int eventId);
    public Event getEventById(int eventId);
    public List<Event> getAllEvents();
    public Event addUserToEvent(int eventId, int userId);
    public List<User> showEventUsers(int eventId);
    public Event updateEventImage(int eventId, MultipartFile eventImage);
    public String getImageUrlForEventByID(int idEvent);
    public List<Event> showEventsByCategory(TypeEvent category);
    public void recordInteraction(int userId, int eventId, InteractionType interactionType);
    public List<EventInteraction> getUserInteractions(int userId);
    public List<EventInteraction> getEventInteractions(int eventId);
    public Event removeUserFromEvent(int eventId, int userId);
    public long getTotalUniqueParticipants();
}
