package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.Event;
import org.example.DAO.Entities.Feedback;
import org.example.DAO.Entities.User;
import org.example.DAO.Repositories.EventRepository;
import org.example.DAO.Repositories.FeedbackRepository;
import org.example.DAO.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class FeedbackService implements FeedbackIService {
    FeedbackRepository feedbackRepository;
    EventRepository eventRepository;
    UserRepository userRepository;

    public Feedback addFeedback(int eventId, int userId, Feedback feedback) {
        Optional<Event> event = eventRepository.findById(eventId);
        Optional<User> user = userRepository.findById(userId);

        if (event.isPresent() && user.isPresent()) {
            feedback.setEvent(event.get());
            feedback.setUser(user.get());
            return feedbackRepository.save(feedback);
        }
        return null; // Retourne null si event ou user non trouvé
    }

    public List<Feedback> getFeedbacksByEvent(int eventId) {
        return feedbackRepository.findByEvent_EventId(eventId);
    }

    public Feedback updateFeedback(int feedbackId, Feedback newFeedback) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(feedbackId);
        if (feedbackOptional.isPresent()) {
            Feedback feedback = feedbackOptional.get();
            feedback.setTitle(newFeedback.getTitle());
            feedback.setDescription(newFeedback.getDescription());
            return feedbackRepository.save(feedback);
        }
        return null; // Retourne null si feedback non trouvé
    }

    public boolean deleteFeedback(int feedbackId) {
        if (feedbackRepository.existsById(feedbackId)) {
            feedbackRepository.deleteById(feedbackId);
            return true;
        }
        return false; // Retourne false si feedback non trouvé
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
}
