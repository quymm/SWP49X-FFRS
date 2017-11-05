package com.services;

import com.entity.TourMatchEntity;
import com.repository.TourMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourMatchServices {
    @Autowired
    TourMatchRepository tourMatchRepository;

    public TourMatchEntity findTourMatchEntityById(int tourMatchId) {
        return tourMatchRepository.findByIdAndStatus(tourMatchId, true);
    }
}
