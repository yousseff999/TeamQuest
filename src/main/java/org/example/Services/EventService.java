package org.example.Services;

import antlr.Utils;
import lombok.AllArgsConstructor;
import org.example.DAO.ENUM.InteractionType;
import org.example.DAO.ENUM.TypeEvent;
import org.example.DAO.Entities.Event;
import org.example.DAO.Entities.EventInteraction;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.EventInteractionRepository;
import org.example.DAO.Repositories.EventRepository;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class EventService implements EventIService{
    EventRepository eventRepository;
    UserRepository userRepository;
    EventInteractionRepository eventInteractionRepository;
    Environment environment;


    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(int eventId, Event updatedEvent) {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            Event event = existingEvent.get();
            event.setEventName(updatedEvent.getEventName());
            event.setDescription(updatedEvent.getDescription());
            event.setStartDate(updatedEvent.getStartDate());
            event.setEndDate(updatedEvent.getEndDate());
            event.setLocation(updatedEvent.getLocation());
            event.setEventImage(updatedEvent.getEventImage());
            event.setEventType(updatedEvent.getEventType());

            return eventRepository.save(event);
        } else {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }
    }

    @Transactional
    public void deleteEvent(int eventId) {
        // Delete related event_interaction records
        eventInteractionRepository.deleteByEventId(eventId);
        // Delete the event
        eventRepository.deleteById(eventId);
    }

    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
    }

    public List<Event> showEventsByCategory(TypeEvent category) {
        return eventRepository.findByEventType(category);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event addUserToEvent(int eventId, int userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        event.getParticipants().add(user);
        return eventRepository.save(event);
    }
    public Event removeUserFromEvent(int eventId, int userId) {
        // Récupérer l'événement par ID
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        // Récupérer l'utilisateur par ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Supprimer l'utilisateur de la liste des participants
        if (event.getParticipants().contains(user)) {
            event.getParticipants().remove(user);
        } else {
            throw new RuntimeException("User is not a participant of the event");
        }

        // Sauvegarder l'événement avec la liste mise à jour
        return eventRepository.save(event);
    }


    public List<User> showEventUsers(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        return event.getParticipants();
    }

    @Override
    public Event updateEventImage(int idEvent, MultipartFile eventImage) {
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        try {
            if (eventImage != null && !eventImage.isEmpty() && eventImage.getSize() > 0) {
                // Generate a unique file name
                String originalFilename = eventImage.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newPhotoName = UUID.randomUUID() + extension; // Unique name

                // Define upload directory
                String uploadDir = environment.getProperty("upload.event.images");
                if (uploadDir == null) {
                    throw new RuntimeException("Upload directory not set in properties.");
                }

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the file
                Path filePath = uploadPath.resolve(newPhotoName);
                Files.copy(eventImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Update event image field
                event.setEventImage(newPhotoName);
            }
            return eventRepository.save(event);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update event photo", e);
        }
    }

    @Override
    public String getImageUrlForEventByID(int idEvent) {
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        String baseUrl = environment.getProperty("export.event.images");
        if (baseUrl == null) {
            throw new RuntimeException("Export image URL not set in properties.");
        }

        String eventImage = event.getEventImage();
        return (eventImage != null && !eventImage.isEmpty()) ? baseUrl + eventImage : null;
    }

    public void recordInteraction(int userId, int eventId, InteractionType interactionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        EventInteraction interaction = EventInteraction.builder()
                .user(user)
                .event(event)
                .interactionType(interactionType)
                .timestamp(LocalDateTime.now())
                .build();

        eventInteractionRepository.save(interaction);
    }

    public List<EventInteraction> getUserInteractions(int userId) {
        return eventInteractionRepository.findByUserId(userId);
    }
    public List<EventInteraction> getEventInteractions(int eventId) {
        return eventInteractionRepository.findByEventEventId(eventId);
    }
}
