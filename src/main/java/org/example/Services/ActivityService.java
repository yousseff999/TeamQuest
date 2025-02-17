package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.Event;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.ActivityRepository;
import org.example.DAO.Repositories.EventRepository;
import org.example.DAO.Repositories.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ActivityService implements ActivityIService{
    ActivityRepository activityRepository;
    EventRepository eventRepository;
    UserRepository userRepository;
    Environment environment;


    @Override
    public Activity updateActivityImage(int activityID, MultipartFile activityImage) {
        Activity activity = activityRepository.findById(activityID)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        try {
            if (activityImage != null && !activityImage.isEmpty() && activityImage.getSize() > 0) {
                // Generate a unique file name
                String originalFilename = activityImage.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newPhotoName = UUID.randomUUID() + extension; // Unique name

                // Define upload directory
                String uploadDir = environment.getProperty("upload.activity.images");
                if (uploadDir == null) {
                    throw new RuntimeException("Upload directory not set in properties.");
                }

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the file
                Path filePath = uploadPath.resolve(newPhotoName);
                Files.copy(activityImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Update activity image field
                activity.setActivityImage(newPhotoName);
            }
            return activityRepository.save(activity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update activity photo", e);
        }
    }

    @Override
    public String getImageUrlForActivityByID(int activityID) {
        Activity activity = activityRepository.findById(activityID)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        String baseUrl = environment.getProperty("export.activity.images");
        if (baseUrl == null) {
            throw new RuntimeException("Export image URL not set in properties.");
        }

        String activityImage = activity.getActivityImage();
        return (activityImage != null && !activityImage.isEmpty()) ? baseUrl + activityImage : null;
    }

    @Override
    public Activity addActivityWithImage(Activity activity, MultipartFile activityImage, int eventId) {
        if (activity.getStartDate().isBefore(activity.getEndDate())) {
            try {
                // Récupérer l'événement spécifique
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new RuntimeException("Event not found"));

                // Associer l'événement à l'activité
                activity.setEvent(event);
                activity.setStatus("Open");
                activity.setCurrentParticipants(0);

                // Sauvegarder l'activité sans image au début
                Activity savedActivity = activityRepository.save(activity);

                // Vérifier si une image est fournie
                if (activityImage != null && !activityImage.isEmpty() && activityImage.getSize() > 0) {
                    String uploadDir = environment.getProperty("upload.activity.images");
                    if (uploadDir == null) {
                        throw new RuntimeException("Upload directory not set in properties.");
                    }

                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // Générer un nom unique pour la nouvelle image
                    String originalFilename = activityImage.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String newPhotoName = UUID.randomUUID() + extension;

                    // Supprimer l'ancienne image si elle existe
                    String oldPhotoName = savedActivity.getActivityImage();
                    if (oldPhotoName != null) {
                        Path oldFilePath = uploadPath.resolve(oldPhotoName);
                        Files.deleteIfExists(oldFilePath);
                    }

                    // Sauvegarder la nouvelle image
                    Path filePath = uploadPath.resolve(newPhotoName);
                    Files.copy(activityImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    // Mettre à jour l'activité avec la nouvelle image
                    savedActivity.setActivityImage(newPhotoName);
                    return activityRepository.save(savedActivity);
                }

                return savedActivity;
            } catch (IOException e) {
                throw new RuntimeException("Failed to add or update activity", e);
            }
        } else {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

    @Override
    public void deleteActivity(int id) {
        Activity activity = activityRepository.findById(id).orElse(null);

        if (activity != null) {
            // Assuming participants are stored in some way within the Activity (e.g., as a count or separate entity)
            if (activity.getCurrentParticipants() > 0) {
                String msg = "We regret to inform you that the activity '"
                        + activity.getActivityName() + "' has been cancelled.";
                System.out.println(msg); // Replace with actual notification logic
            }

            // Deleting the activity
            activityRepository.delete(activity);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void autoCloseActivities() {
        List<Activity> activities = activityRepository.findActivitiesByStartDateIsBefore(LocalDate.now());
        for (Activity activity : activities) {
            if (!Objects.equals(activity.getStatus(), "Closed")) {
                activity.setStatus("Closed");
                System.err.println(activity.getActivityName());
                activityRepository.save(activity);
            }
        }
    }

    @Override
    public void maxParticipantsAttended() {
        List<Activity> activities = activityRepository.findByStatus("Open");
        for (Activity activity : activities) {
            if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
                activity.setStatus("Closed");
                activityRepository.save(activity); // Save changes to the database
            }
        }
    }
    // Scheduled task to run every midnight
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void autoCloseActivitiesWhenFull() {
        maxParticipantsAttended();
    }

    public List<Activity> showActivities() {
        List<Activity> activities = activityRepository.findAll();
        List<Activity> output = new ArrayList<>();
        for (Activity activity : activities) {
            if (Objects.equals(activity.getStatus(), "Open")) {
                output.add(activity);
            }
        }
        return output;
    }
    public List<Activity> showAllActivities() {
        return activityRepository.findAll();
    }

    public List<Activity> searchActivityByName(String activityName) {
        List<Activity> activities = activityRepository.findActivitiesByActivityNameContainingIgnoreCase(activityName);
        List<Activity> output = new ArrayList<>();
        for (Activity activity : activities) {
            if (Objects.equals(activity.getStatus(), "Open"))
                output.add(activity);
        }
        return output;
    }

    @Override
    public List<Activity> searchActivityByStartDate(LocalDate startDate) {
        return activityRepository.findActivitiesByStartDateBefore(startDate);
    }

    // Register the user to an activity within an event, if they are already registered for that event
    public void registerUserToActivity(int userId, int activityId, int eventId) {
        // Fetch the user, event, and activity objects
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        // Check if the user is registered for the event
        if (!isUserRegisteredForEvent(user, event)) {
            throw new RuntimeException("User is not registered for this event.");
        }

        // Check if the activity is already full
        if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            throw new RuntimeException("Activity is full, cannot register.");
        }
        // Check if the user is already registered for the activity
        if (activity.getUsers().contains(user)) {
            throw new RuntimeException("User is already registered for this activity.");
        }

        // Register the user to the activity
        activity.getUsers().add(user);

        // Increment the number of current participants
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);

        // Save the activity (which saves the relationship)
        activityRepository.save(activity);
    }
    // Helper method to check if the user is registered for the event
    private boolean isUserRegisteredForEvent(User user, Event event) {
        return event.getParticipants().contains(user);  // Check if user is in the participants list of the event
    }

    public List<User> getUsersByActivity(int activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        return new ArrayList<>(activity.getUsers()); // Retourne la liste des utilisateurs
    }

}

