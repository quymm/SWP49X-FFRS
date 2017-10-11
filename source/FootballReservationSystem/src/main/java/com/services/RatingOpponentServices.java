package com.services;

import com.entity.AccountEntity;
import com.entity.RatingOpponentEntity;
import com.entity.TourMatchEntity;
import com.repository.RatingOpponentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class RatingOpponentServices {
    @Autowired
    AccountServices accountServices;

    @Autowired
    TourMatchServices tourMatchServices;

    @Autowired
    RatingOpponentRepository ratingOpponentRepository;


    public List<RatingOpponentEntity> findByUserId(int userId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(userId, "user");
        return ratingOpponentRepository.findByUserIdAndStatus(accountEntity, true);
    }
    public List<RatingOpponentEntity> findByOpponentId(int opponentId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(opponentId, "user");
        return ratingOpponentRepository.findByOpponentIdAndStatus(accountEntity, true);
    }
    public List<RatingOpponentEntity> findBytourMatchId(int tourMatchId){
        TourMatchEntity tourMatchEntity = tourMatchServices.findTourMatchEntityById(tourMatchId);
        return ratingOpponentRepository.findByTourMatchIdAndStatus(tourMatchEntity, true);
    }

    public  RatingOpponentEntity findByUserIdAndOpponentIdAndTourMatchIdAndStatus(int userId, int opponentId, int tourMatchId, boolean status){
        AccountEntity userAccountEntity = accountServices.findAccountEntityById(userId, "user");
        AccountEntity opponentAccountEntity = accountServices.findAccountEntityById(opponentId, "user");
        TourMatchEntity tourMatchEntity = tourMatchServices.findTourMatchEntityById(tourMatchId);
        return ratingOpponentRepository.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(userAccountEntity, opponentAccountEntity, tourMatchEntity,true);
    }

}
