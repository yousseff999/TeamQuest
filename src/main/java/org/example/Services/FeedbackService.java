package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Repositories.FeedbackRepository;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class FeedbackService implements FeedbackIService {
    FeedbackRepository FeedbackRepository;
}
