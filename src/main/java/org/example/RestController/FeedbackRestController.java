package org.example.RestController;

import lombok.AllArgsConstructor;
import org.example.DAO.Entities.ContactDTO;
import org.example.DAO.Entities.Feedback;
import org.example.Services.EmailService;
import org.example.Services.FeedbackIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@RestController
@AllArgsConstructor
@RequestMapping("/Feedback")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackRestController {
    FeedbackIService feedbackService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/sendemail")
    public ResponseEntity<String> sendContactForm(@RequestBody ContactDTO contact) {
        String to = "youssef.zammit999@gmail.com"; // Replace with your target recipient
        String subject = "Contact Form: " + contact.getSubject();

        String text = """
            <h3>New Contact Form Submission</h3>
            <p><strong>Name:</strong> %s</p>
            <p><strong>Email:</strong> %s</p>
            <p><strong>Message:</strong><br/>%s</p>
        """.formatted(contact.getName(), contact.getEmail(), contact.getMessage());

        emailService.sendEmail(to, subject, text);
        return ResponseEntity.ok("Message sent successfully");
    }
    @PostMapping("/add/{eventId}/{userId}")
    public ResponseEntity<Feedback> addFeedback(@PathVariable int eventId, @PathVariable int userId, @RequestBody Feedback feedback) {
        Feedback createdFeedback = feedbackService.addFeedback(eventId, userId, feedback);
        if (createdFeedback != null) {
            return ResponseEntity.ok(createdFeedback);
        }
        return ResponseEntity.badRequest().build(); // Si event ou user non trouvé
    }

    // Récupérer tous les feedbacks d'un événement
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByEvent(@PathVariable int eventId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByEvent(eventId);
        return ResponseEntity.ok(feedbacks);
    }

    @PutMapping("/update/{feedbackId}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable int feedbackId, @RequestBody Feedback feedback) {
        Feedback updatedFeedback = feedbackService.updateFeedback(feedbackId, feedback);
        if (updatedFeedback != null) {
            return ResponseEntity.ok(updatedFeedback);
        }
        return ResponseEntity.notFound().build(); // Si feedback non trouvé
    }

    @DeleteMapping("/delete/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable int feedbackId) {
        if (feedbackService.deleteFeedback(feedbackId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build(); // Si feedback non trouvé
    }

    @GetMapping("/getall")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }
}
