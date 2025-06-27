package org.example.Services;

import org.example.DAO.Entities.Activity;
import org.example.DAO.Entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface ActivityIService {
    public Activity updateActivityImage(int activityID, MultipartFile activityImage);
    public Activity updateActivity(int activityID, Activity updatedActivity);
    public Activity getActivityById(int activityID);
    public String getImageUrlForActivityByID(int activityID);
    public Activity addActivityWithImage(Activity activity, MultipartFile activityImage, int eventId);
    public void autoCloseActivities();
    public void maxParticipantsAttended();
    public void deleteActivity(int id);
    public List<Activity> showActivities();
    public List<Activity> showAllActivities();
    public List<Activity> searchActivityByName(String activityName);
    public List<Activity> searchActivityByStartDate(LocalDate startDate);
    public void registerUserToActivity(int userId, int activityId, int eventId);
    public List<User> getUsersByActivity(int activityId);
    public List<Activity> getActivitiesByEventId(int eventId);
}
