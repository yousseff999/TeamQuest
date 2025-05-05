package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.Event;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.EventRepository;
import org.example.Services.ActivityIService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Activity")
@CrossOrigin(origins = "http://localhost:4200")
public class ActivityRestController {
    ActivityIService activityIService;
    EventRepository eventRepository;

    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<Activity> updateActivityImage(
            @PathVariable("id") int activityID,
            @RequestParam("image") MultipartFile activityImage) {
        Activity updatedActivity = activityIService.updateActivityImage(activityID, activityImage);
        return ResponseEntity.ok(updatedActivity);
    }
    @PutMapping("/{id}/update")
    public ResponseEntity<Activity> updateActivity(
            @PathVariable("id") int activityID,
            @RequestBody Activity updatedActivity) {
        Activity result = activityIService.updateActivity(activityID, updatedActivity);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable("id") int activityID) {
        Activity activity = activityIService.getActivityById(activityID);
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<String> getActivityImageUrl(@PathVariable("id") int activityID) {
        String imageUrl = activityIService.getImageUrlForActivityByID(activityID);
        return ResponseEntity.ok(imageUrl);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addActivityWithImage(
            @RequestParam("eventId") int eventId,
            @RequestParam("activityName") String activityName,
            @RequestParam("description") String description,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @RequestParam("maxParticipants") int maxParticipants,
            @RequestParam("activityImage") MultipartFile activityImage) {

        try {
            // Trim whitespace from date strings
            startDateStr = startDateStr.trim();
            endDateStr = endDateStr.trim();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate;
            LocalDate endDate;

            // Parse the dates from string to LocalDate
            try {
                startDate = LocalDate.parse(startDateStr, formatter);
                endDate = LocalDate.parse(endDateStr, formatter);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body("Invalid date format. Use YYYY-MM-DD.");
            }

            // Ensure the end date is after the start date
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().body("End date must be after start date.");
            }

            // Retrieve the event by its ID
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            // Build the Activity entity
            Activity activity = Activity.builder()
                    .activityName(activityName)
                    .description(description)
                    .startDate(startDate)
                    .endDate(endDate)
                    .maxParticipants(maxParticipants)
                    .event(event)
                    .currentParticipants(0)  // assuming initial participants count is 0
                    .status("Open")  // assuming the initial status is Open
                    .build();

            // Add the activity with image
            Activity savedActivity = activityIService.addActivityWithImage(activity, activityImage, eventId);
            return ResponseEntity.ok(savedActivity);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable int id) {
        activityIService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/show_open")
    public ResponseEntity<List<Activity>> getOpenActivities() {
        List<Activity> openActivities = activityIService.showActivities();
        return ResponseEntity.ok(openActivities);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityIService.showAllActivities();
        return ResponseEntity.ok(activities);
    }
    @GetMapping("/searchByName")
    public ResponseEntity<List<Activity>> searchActivities(@RequestParam String name) {
        List<Activity> activities = activityIService.searchActivityByName(name);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/searchByStartDate")
    public List<Activity> searchActivityByStartDate(
            @RequestParam(value = "startdate", required = false) String startdateString) {

        if (startdateString == null || startdateString.trim().isEmpty()) {
            return new ArrayList<>();  // or some default handling when no date is provided
        }

        // Trim any unwanted spaces
        startdateString = startdateString.trim();

        try {
            // Parse the date to LocalDate
            LocalDate startdate = LocalDate.parse(startdateString);
            return activityIService.searchActivityByStartDate(startdate);
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            throw new IllegalArgumentException("Invalid date format, please use YYYY-MM-DD");
        }
    }

    @PostMapping("/registerUserToActivity/{activityId}/register/{userId}/event/{eventId}")
    public ResponseEntity<String> registerUserToActivity(
            @PathVariable int userId,
            @PathVariable int activityId,
            @PathVariable int eventId) {

        try {
            activityIService.registerUserToActivity(userId, activityId, eventId);
            return ResponseEntity.ok("User registered successfully to the activity.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{activityId}/users")
    public ResponseEntity<List<User>> getUsersByActivity(@PathVariable int activityId) {
        try {
            List<User> users = activityIService.getUsersByActivity(activityId);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
}
