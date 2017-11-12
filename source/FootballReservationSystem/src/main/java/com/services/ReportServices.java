package com.services;

import com.config.Constant;
import com.dto.InputReportFieldDTO;
import com.dto.InputReportOpponentDTO;
import com.entity.AccountEntity;
import com.entity.ReportFieldEntity;
import com.entity.ReportOpponentEntity;
import com.entity.TourMatchEntity;
import com.repository.ProfileRepository;
import com.repository.ReportFieldRepository;
import com.repository.ReportOpponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServices {
    @Autowired
    ReportOpponentRepository reportOpponentRepository;

    @Autowired
    ReportFieldRepository reportFieldRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    MatchServices matchServices;

    @Autowired
    Constant constant;

    public ReportOpponentEntity reportOpponent(InputReportOpponentDTO inputReportOpponentDTO){
        AccountEntity user = accountServices.findAccountEntityById(inputReportOpponentDTO.getUserId(), constant.getUserRole());
        AccountEntity opponent = accountServices.findAccountEntityById(inputReportOpponentDTO.getUserId(), constant.getUserRole());

        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(inputReportOpponentDTO.getTourMatchId());

        if(inputReportOpponentDTO.getReason().isEmpty()){
            throw new IllegalArgumentException("Report must have reason content!");
        }
        ReportOpponentEntity reportOpponentEntity = new ReportOpponentEntity();
        reportOpponentEntity.setUserId(user);
        reportOpponentEntity.setOpponentId(opponent);
        reportOpponentEntity.setTourMatchId(tourMatchEntity);
        reportOpponentEntity.setReason(inputReportOpponentDTO.getReason());
        reportOpponentEntity.setStatus(true);

        ReportOpponentEntity savedReportOpponentEntity = reportOpponentRepository.save(reportOpponentEntity);
        // add thêm 1 lần bị report vào profile của người chơi bị report
        opponent.getProfileId().setNumOfReport(opponent.getProfileId().getNumOfReport() + 1);
        profileRepository.save(opponent.getProfileId());
        return savedReportOpponentEntity;
    }

    public List<ReportOpponentEntity> getReportOfUser(int userId){
        AccountEntity user = accountServices.findAccountEntityById(userId, constant.getUserRole());
        return reportOpponentRepository.findByOpponentIdAndStatus(user, true);
    }

    public ReportFieldEntity reportField(InputReportFieldDTO inputReportFieldDTO){
        AccountEntity user = accountServices.findAccountEntityById(inputReportFieldDTO.getUserId(), constant.getUserRole());
        AccountEntity fieldOwner = accountServices.findAccountEntityById(inputReportFieldDTO.getFieldOwnerId(), constant.getFieldOwnerRole());
        if(inputReportFieldDTO.getReason().isEmpty()){
            throw new IllegalArgumentException("Report must have reason content!");
        }
        ReportFieldEntity reportFieldEntity = new ReportFieldEntity();
        reportFieldEntity.setUserId(user);
        reportFieldEntity.setFieldOwnerId(fieldOwner);
        reportFieldEntity.setReason(inputReportFieldDTO.getReason());
        reportFieldEntity.setStatus(true);
//        reportFieldEntity.setCreationDate(new Date());
//        reportFieldEntity.setModificationDate(new Date());

        ReportFieldEntity savedreportFieldEntity = reportFieldRepository.save(reportFieldEntity);
        // add thêm 1 lần bị report vào profile của chủ sân bị report
        fieldOwner.getProfileId().setNumOfReport(fieldOwner.getProfileId().getNumOfReport() + 1);
        profileRepository.save(fieldOwner.getProfileId());

        return reportFieldRepository.save(reportFieldEntity);
    }

    public List<ReportFieldEntity> getReportOfFieldOwner(int fieldOwnerId){
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, constant.getFieldOwnerRole());
        return reportFieldRepository.findByFieldOwnerIdAndStatus(fieldOwner, true);
    }

    public List<ReportOpponentEntity> getListReportOrderByNumberReport(){
        return reportOpponentRepository.getListReportOfUser();
    }
}
