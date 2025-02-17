package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.TypeEvent;
import org.example.DAO.Entities.Event;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.EventRepository;
import org.example.Services.EventIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Event")
@CrossOrigin(origins = "http://localhost:4200")
public class EventRestController {
    EventIService eventIService;

    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        Event newEvent = eventIService.addEvent(event);
        return ResponseEntity.ok(newEvent);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable int eventId, @RequestBody Event updatedEvent) {
        Event event = eventIService.updateEvent(eventId, updatedEvent);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable int eventId) {
        eventIService.deleteEvent(eventId);
        return ResponseEntity.ok("Event deleted successfully.");
    }

    @GetMapping("/category/{category}")
    public List<Event> getEventsByCategory(@PathVariable TypeEvent category) {
        return eventIService.showEventsByCategory(category);
    }

    @GetMapping("/getevent/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable int eventId) {
        Event event = eventIService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventIService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/{eventId}/addUser/{userId}")
    public ResponseEntity<Event> addUserToEvent(@PathVariable int eventId, @PathVariable int userId) {
        Event event = eventIService.addUserToEvent(eventId, userId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/{eventId}/users")
    public ResponseEntity<List<User>> showEventUsers(@PathVariable int eventId) {
        List<User> users = eventIService.showEventUsers(eventId);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{idEvent}/upload-image")
    public ResponseEntity<Event> updateEventImage(
            @PathVariable int idEvent,
            @RequestParam("eventImage") MultipartFile eventImage) {
        Event updatedEvent = eventIService.updateEventImage(idEvent, eventImage);
        return ResponseEntity.ok(updatedEvent);
    }

    @GetMapping("/{idEvent}/image-url")
    public ResponseEntity<String> getImageUrlForEvent(@PathVariable int idEvent) {
        String imageUrl = eventIService.getImageUrlForEventByID(idEvent);
        return ResponseEntity.ok(imageUrl);
    }
}
