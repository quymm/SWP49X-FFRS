package com.services;

import com.dto.InputReportOpponentDTO;
import com.entity.AccountEntity;
import com.entity.ReportOpponentEntity;
import com.entity.TourMatchEntity;
import com.repository.ReportOpponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportOpponentServices {
    @Autowired
    AccountServices accountServices;

    @Autowired
    TourMatchServices tourMatchServices;

    @Autowired
    ReportOpponentRepository reportOpponentRepository;

//    public List<RatingOpponentEntity> findByUserId(int userId){
//        AccountEntity accountEntity = accountServices.findAccountEntityById(userId, "user");
//        return ratingOpponentRepository.findByUserIdAndStatus(accountEntity, true);
//    }
//    public List<RatingOpponentEntity> findByOpponentId(int opponentId){
//        AccountEntity accountEntity = accountServices.findAccountEntityById(opponentId, "user");
//        return ratingOpponentRepository.findByOpponentIdAndStatus(accountEntity, true);
//    }
//    public List<RatingOpponentEntity> findBytourMatchId(int tourMatchId){
//        TourMatchEntity tourMatchEntity = tourMatchServices.findTourMatchEntityById(tourMatchId);
//        return ratingOpponentRepository.findByTourMatchIdAndStatus(tourMatchEntity, true);
//    }
    public List<ReportOpponentEntity> findByUserId(int userId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(userId, "user");
        return reportOpponentRepository.findByUserIdAndStatus(accountEntity,true);
    }

    public List<ReportOpponentEntity> findByOpponentId(int opponentId){
        AccountEntity accountEntity = accountServices.findAccountEntityById(opponentId, "user");
        return reportOpponentRepository.findByOpponentIdAndStatus(accountEntity,true);
    }

    public List<ReportOpponentEntity> findByTourMatchId(int tourMatchId){
        TourMatchEntity tourMatchEntity = tourMatchServices.findTourMatchEntityById(tourMatchId);
        return reportOpponentRepository.findByTourMatchIdAndStatus(tourMatchEntity,true);
    }

    public  ReportOpponentEntity findByUserIdAndOpponentIdAndTourMatchIdAndStatus(int userId, int opponentId, int tourMatchId, boolean status){
        AccountEntity userAccountEntity = accountServices.findAccountEntityById(userId, "user");
        AccountEntity opponentAccountEntity = accountServices.findAccountEntityById(opponentId, "user");
        TourMatchEntity tourMatchEntity = tourMatchServices.findTourMatchEntityById(tourMatchId);
        return reportOpponentRepository.findByUserIdAndOpponentIdAndTourMatchIdAndStatus(userAccountEntity, opponentAccountEntity, tourMatchEntity, true);
    }

    public ReportOpponentEntity createNewReportOpponent(InputReportOpponentDTO inputReportOpponentDTO){
        ReportOpponentEntity reportOpponentEntity = new ReportOpponentEntity();
        AccountEntity userAccountEntity = accountServices.findAccountEntityById(inputReportOpponentDTO.getUserId(), "user");
        AccountEntity opponentAccountEntity = accountServices.findAccountEntityById(inputReportOpponentDTO.getOpponentId(), "user");
        TourMatchEntity tourMatchEntity = tourMatchServices.findTourMatchEntityById(inputReportOpponentDTO.getTourMatchId());
        reportOpponentEntity.setUserId(userAccountEntity);
        reportOpponentEntity.setOpponentId(opponentAccountEntity);
        reportOpponentEntity.setTourMatchId(tourMatchEntity);
        reportOpponentEntity.setReason(inputReportOpponentDTO.getReason());
        reportOpponentEntity.setStatus(true);
        return  reportOpponentRepository.save(reportOpponentEntity);
    }

    public ReportOpponentEntity disableReport(int userId, int opponentId, int tourMatchId, boolean status){
        ReportOpponentEntity reportOpponentEntity = findByUserIdAndOpponentIdAndTourMatchIdAndStatus(userId, opponentId, tourMatchId, true);
        reportOpponentEntity.setStatus(false);
        return reportOpponentRepository.save(reportOpponentEntity);
    }
}
