package com.services;

import com.dto.InputRatingOpponentDTO;
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
    MatchServices matchServices;


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
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(tourMatchId);
        return ratingOpponentRepository.findByTourMatchIdAndStatus(tourMatchEntity, true);
    }

    public  RatingOpponentEntity findByUserIdAndOpponentIdAndTourMatchIdAndStatus(int userId, int opponentId, int tourMatchId, boolean status){
        AccountEntity userAccountEntity = accountServices.findAccountEntityById(userId, "user");
        AccountEntity opponentAccountEntity = accountServices.findAccountEntityById(opponentId, "user");
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(tourMatchId);
        return ratingOpponentRepository.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(userAccountEntity, opponentAccountEntity, tourMatchEntity,true);
    }

    public RatingOpponentEntity createNewRatingOpponent(InputRatingOpponentDTO inputRatingOpponentDTO){
        RatingOpponentEntity ratingOpponentEntity = new RatingOpponentEntity();
        AccountEntity userAccountEntity = accountServices.findAccountEntityById(inputRatingOpponentDTO.getUserId(), "user");
        AccountEntity opponentAccountEntity = accountServices.findAccountEntityById(inputRatingOpponentDTO.getOpponentId(), "user");
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(inputRatingOpponentDTO.getTourMatchId());
        ratingOpponentEntity.setUserId(userAccountEntity);
        ratingOpponentEntity.setOpponentId(opponentAccountEntity);
        ratingOpponentEntity.setTourMatchId(tourMatchEntity);
        ratingOpponentEntity.setRatingScore(inputRatingOpponentDTO.getRatingScore());
        ratingOpponentEntity.setWin(inputRatingOpponentDTO.isWin());
        ratingOpponentEntity.setStatus(true);
        return  ratingOpponentRepository.save(ratingOpponentEntity);
    }
}
