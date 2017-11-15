package com.services;

import com.config.Constant;
import com.dto.InputRatingOpponentDTO;
import com.entity.AccountEntity;
import com.entity.ProfileEntity;
import com.entity.RatingOpponentEntity;
import com.entity.TourMatchEntity;
import com.repository.ProfileRepository;
import com.repository.RatingOpponentRepository;
import com.repository.TourMatchRepository;
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

    @Autowired
    TourMatchRepository tourMatchRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    Constant constant;


    public List<RatingOpponentEntity> findByUserId(int userId) {
        AccountEntity accountEntity = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        return ratingOpponentRepository.findByUserIdAndStatus(accountEntity, true);
    }

    public List<RatingOpponentEntity> findBytourMatchId(int tourMatchId) {
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(tourMatchId);
        return ratingOpponentRepository.findByTourMatchIdAndStatus(tourMatchEntity, true);
    }

    public RatingOpponentEntity findByUserIdAndTourMatchId(int userId, int tourMatchId) {
        AccountEntity userAccountEntity = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(tourMatchId);
        return ratingOpponentRepository.findByUserIdAndTourMatchIdAndStatus(userAccountEntity, tourMatchEntity, true);
    }

    public RatingOpponentEntity findById(int ratingId) {
        RatingOpponentEntity ratingOpponentEntity = ratingOpponentRepository.findByIdAndStatus(ratingId, true);
        if (ratingOpponentEntity == null) {
            throw new EntityNotFoundException(String.format("Not found Rating Opponent have id = %s", ratingId));
        }
        return ratingOpponentEntity;
    }

    public RatingOpponentEntity createNewRatingOpponent(InputRatingOpponentDTO inputRatingOpponentDTO) {
        RatingOpponentEntity ratingOpponentEntity = new RatingOpponentEntity();
        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(inputRatingOpponentDTO.getTourMatchId());

        AccountEntity user = accountServices.findAccountEntityByIdAndRole(inputRatingOpponentDTO.getUserId(), constant.getUserRole());
        ratingOpponentEntity.setUserId(user);
        ratingOpponentEntity.setTourMatchId(tourMatchEntity);
        ratingOpponentEntity.setRatingLevel(inputRatingOpponentDTO.getRatingLevel());
        ratingOpponentEntity.setResult(inputRatingOpponentDTO.getResult());
        if (inputRatingOpponentDTO.getGoalsDifference() != null) {
            ratingOpponentEntity.setGoalsDifference(inputRatingOpponentDTO.getGoalsDifference());
        }
        ratingOpponentEntity.setStatus(true);
        RatingOpponentEntity savedRatingOpponentEntity = ratingOpponentRepository.save(ratingOpponentEntity);

        // nếu đã đủ cả 2 đánh giá thì thực hiện
        if (!findBytourMatchId(tourMatchEntity.getId()).isEmpty()
                && findBytourMatchId(tourMatchEntity.getId()).size() == 2) {
            tourMatchEntity.setCompleteStatus(true);
            tourMatchRepository.save(tourMatchEntity);
            calculateRatingPointAndBonusPoint(tourMatchEntity);
        }
        return savedRatingOpponentEntity;
    }

    public void calculateRatingPointAndBonusPoint(TourMatchEntity tourMatchEntity) {
        AccountEntity user = tourMatchEntity.getUserId();
        AccountEntity opponent = tourMatchEntity.getOpponentId();
        ProfileEntity userProfile = user.getProfileId();
        ProfileEntity opponentProfile = opponent.getProfileId();
        if (tourMatchEntity.getCompleteStatus()) {
            RatingOpponentEntity ratingOpponent = findByUserIdAndTourMatchId(user.getId(), tourMatchEntity.getId());
            RatingOpponentEntity ratingUser = findByUserIdAndTourMatchId(opponent.getId(), tourMatchEntity.getId());
            // Kiểm tra người dùng gửi về kết quả có khớp với nhau hay ko?
            boolean ratingRight = false;
            int bonusRatingScore = 0; // nếu đánh giá về tỉ số đúng thì sẽ được 2đ bonus point

            int goalsDifference = 0; // hiệu số bàn thắng bại
            // nếu đánh giá của 2 đối thủ về hiệu số bàn thắng bại là giống nhau thì sẽ ảnh hưởng điểm ranking
            if (ratingUser.getGoalsDifference() != null && ratingOpponent.getGoalsDifference() != null
                    && ratingUser.getGoalsDifference() == ratingOpponent.getGoalsDifference()) {
                goalsDifference = ratingUser.getGoalsDifference();
                bonusRatingScore = 3;
            }
            // rating level
            // 0: đá tệ
            // 2: đá bình thường
            // 4: đá hay
            if (ratingUser.getResult() == 1 && ratingOpponent.getResult() == 1) {
                // Trận đấu có kết quả hòa
                userProfile.setRatingScore(userProfile.getRatingScore() + 10 + ratingUser.getRatingLevel());
                opponentProfile.setRatingScore(opponentProfile.getRatingScore() + 10 + ratingOpponent.getRatingLevel());
                ratingRight = true;
            } else if (ratingUser.getResult() == 3 && ratingOpponent.getResult() == 0) {
                // User là người thắng
                userProfile.setRatingScore(userProfile.getRatingScore() + 30 + goalsDifference + ratingUser.getRatingLevel());
                if (opponentProfile.getRatingScore() + ratingOpponent.getRatingLevel() >= goalsDifference) {
                    opponentProfile.setRatingScore(opponentProfile.getRatingScore() - goalsDifference + ratingOpponent.getRatingLevel());
                } else {
                    opponentProfile.setRatingScore(0);
                }
                ratingRight = true;
            } else if (ratingUser.getResult() == 1 && ratingOpponent.getResult() == 3) {
                // Opponent là người thắng
                if (userProfile.getRatingScore() + ratingUser.getRatingLevel() >= goalsDifference) {
                    userProfile.setRatingScore(user.getProfileId().getRatingScore() - goalsDifference + ratingUser.getRatingLevel());
                } else {
                    userProfile.setRatingScore(0);
                }
                opponentProfile.setRatingScore(opponent.getProfileId().getRatingScore() + 30 + goalsDifference);
                ratingRight = true;
            } else {
                userProfile.setRatingScore(userProfile.getRatingScore() + ratingUser.getRatingLevel());
                opponentProfile.setRatingScore(opponentProfile.getRatingScore() + ratingOpponent.getRatingLevel());
            }
            // duoc cong diem bonus point nếu kết quả đánh giá khớp
            if (ratingRight) {
                userProfile.setBonusPoint(userProfile.getBonusPoint() + 10 + bonusRatingScore);
                opponentProfile.setBonusPoint(opponentProfile.getBonusPoint() + 10 + bonusRatingScore);
            }
            profileRepository.save(userProfile);
            profileRepository.save(opponentProfile);
        } else {
            throw new IllegalArgumentException(String.format("Tour Match have id = %s is not complete! No result to calculate rating", tourMatchEntity.getId()));
        }
    }
}
