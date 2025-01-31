package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Repositories.ChallengeRepository;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ChallengeService implements ChallengeIService {
    ChallengeRepository challengeRepository;
}
