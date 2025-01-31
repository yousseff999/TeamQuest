package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Repositories.ActivityRepository;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ActivityService implements ActivityIService{
    ActivityRepository activityRepository;
}
