package org.example.Services;

import org.example.DAO.Entities.Event;
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
}
