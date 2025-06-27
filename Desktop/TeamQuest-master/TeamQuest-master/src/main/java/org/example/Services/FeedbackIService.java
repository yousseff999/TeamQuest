package org.example.Services;

import org.example.DAO.Entities.Feedback;

import java.util.List;

public interface FeedbackIService {
    public Feedback addFeedback(int eventId, int userId, Feedback feedback);
    public List<Feedback> getFeedbacksByEvent(int eventId);
    public Feedback updateFeedback(int feedbackId, Feedback newFeedback);
    public boolean deleteFeedback(int feedbackId);
    public List<Feedback> getAllFeedbacks();
}
