package com.services;

import com.dto.InputRatingOpponentDTO;
import com.entity.AccountEntity;
import com.entity.RatingOpponentEntity;
import com.entity.TourMatchEntity;
import com.repository.RatingOpponentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RatingOpponentServices {
    @Autowired
    AccountServices accountServices;

    @Autowired
    MatchServices matchServices;


    @Autowired
    RatingOpponentRepository ratingOpponentRepository;


    public List<RatingOpponentEntity> findByUserId(int userId) {
        AccountEntity accountEntity = accountServices.findAccountEntityById(userId, "user");
        return ratingOpponentRepository.findByUserIdAndStatus(accountEntity, true);
    }

    public List<RatingOpponentEntity> findByOpponentId(int opponentId) {
        AccountEntity accountEntity = accountServices.findAccountEntityById(opponentId, "user");
        return ratingOpponentRepository.findByOpponentIdAndStatus(accountEntity, true);
    }

    public List<RatingOpponentEntity> findBytourMatchId(int tourMatchId) {
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(tourMatchId);
        return ratingOpponentRepository.findByTourMatchIdAndStatus(tourMatchEntity, true);
    }

    public RatingOpponentEntity findByUserIdAndOpponentIdAndTourMatchIdAndStatus(int userId, int opponentId, int tourMatchId, boolean status) {
        AccountEntity userAccountEntity = accountServices.findAccountEntityById(userId, "user");
        AccountEntity opponentAccountEntity = accountServices.findAccountEntityById(opponentId, "user");
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(tourMatchId);
        return ratingOpponentRepository.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(userAccountEntity, opponentAccountEntity, tourMatchEntity, true);
    }

    public RatingOpponentEntity findById(int ratingId){
        RatingOpponentEntity ratingOpponentEntity = ratingOpponentRepository.findByIdAndStatus(ratingId, true);
        if(ratingOpponentEntity == null){
            throw new EntityNotFoundException(String.format("Not found Rating Opponent have id = %s", ratingId));
        }
        return ratingOpponentEntity;
    }

    public RatingOpponentEntity createNewRatingOpponent(InputRatingOpponentDTO inputRatingOpponentDTO) {
        RatingOpponentEntity ratingOpponentEntity = new RatingOpponentEntity();
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(inputRatingOpponentDTO.getTourMatchId());

        AccountEntity user = accountServices.findAccountEntityById(inputRatingOpponentDTO.getUserId(), "user");
        ratingOpponentEntity.setUserId(user);
        if (user.getId() == tourMatchEntity.getUserId().getId()) {
            ratingOpponentEntity.setOpponentId(tourMatchEntity.getOpponentId());
        } else {
            ratingOpponentEntity.setOpponentId(tourMatchEntity.getUserId());
        }
        ratingOpponentEntity.setTourMatchId(tourMatchEntity);
        ratingOpponentEntity.setRatingScore(inputRatingOpponentDTO.getRatingScore());
        ratingOpponentEntity.setWin(inputRatingOpponentDTO.isWin());
        ratingOpponentEntity.setStatus(true);
        return ratingOpponentRepository.save(ratingOpponentEntity);
    }

    public void calculateRatingPointAndBonusPoint(int tourMatchId) {
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(tourMatchId);
        AccountEntity user = tourMatchEntity.getUserId();
        AccountEntity opponent = tourMatchEntity.getOpponentId();
        if (tourMatchEntity.getCompleteStatus()) {
            RatingOpponentEntity ratingOpponent = ratingOpponentRepository.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(user, opponent, tourMatchEntity, true);
            RatingOpponentEntity ratingUser = ratingOpponentRepository.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(opponent, user, tourMatchEntity, true);
            // kết quả đánh giá đối lập nhau
            if (ratingOpponent.getWin() != ratingUser.getWin()) {
                if (ratingOpponent.getWin()) {
                    // user la nguoi thang
                    user.getProfileId().setRatingScore(user.getProfileId().getRatingScore() + 20);
                    opponent.getProfileId().setRatingScore(opponent.getProfileId().getRatingScore() - 20);

                } else {
                    // user la nguoi thua
                    user.getProfileId().setRatingScore(user.getProfileId().getRatingScore() - 20);
                    opponent.getProfileId().setRatingScore(opponent.getProfileId().getRatingScore() + 20);

                }
                // duoc cong diem bonus point
                user.getProfileId().setBonusPoint(user.getProfileId().getBonusPoint() + 10);
                opponent.getProfileId().setBonusPoint(user.getProfileId().getBonusPoint() + 10);
            }
        } else {
            throw new IllegalArgumentException(String.format("Tour Match have id = %s is not complete! No result to calculate rating", tourMatchEntity.getId()));
        }
    }
}
