package org.example.Services;

import lombok.AllArgsConstructor;
import org.example.DAO.Repositories.RankRepository;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class RankService implements RankIService{
    RankRepository rankRepository;
}
