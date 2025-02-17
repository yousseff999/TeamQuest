package org.example.DAO.Repositories;

import org.example.DAO.Entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    List<Activity> findActivitiesByStartDateIsBefore(LocalDate date);
    List<Activity> findByStatus(String status);
    List<Activity> findActivitiesByActivityNameContainingIgnoreCase(String activityName);
    List<Activity> findActivitiesByStartDateBefore(LocalDate startDate);

}
