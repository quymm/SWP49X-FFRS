package com.services;

import com.config.Constant;
import com.dto.InputReportFieldDTO;
import com.dto.InputReportOpponentDTO;
import com.entity.AccountEntity;
import com.entity.ReportEntity;
import com.entity.TourMatchEntity;
import com.repository.AccountRepository;
import com.repository.ProfileRepository;
import com.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServices {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    MatchServices matchServices;

    @Autowired
    Constant constant;

    public ReportEntity reportOpponent(InputReportOpponentDTO inputReportOpponentDTO){
        AccountEntity accuser = accountServices.findAccountEntityByIdAndRole(inputReportOpponentDTO.getAccusedId(), constant.getUserRole());
        AccountEntity accused = accountServices.findAccountEntityByIdAndRole(inputReportOpponentDTO.getAccusedId(), constant.getUserRole());

        TourMatchEntity tourMatchEntity = matchServices.findTourMatchEntityById(inputReportOpponentDTO.getTourMatchId());

        if(inputReportOpponentDTO.getReason().isEmpty()){
            throw new IllegalArgumentException("Report must have reason content!");
        }
        ReportEntity reportOpponentEntity = new ReportEntity();
        reportOpponentEntity.setAccuserId(accuser);
        reportOpponentEntity.setAccusedId(accused);
        reportOpponentEntity.setTourMatchId(tourMatchEntity);
        reportOpponentEntity.setReason(inputReportOpponentDTO.getReason());
        reportOpponentEntity.setStatus(true);

        ReportEntity savedReportOpponentEntity = reportRepository.save(reportOpponentEntity);
        // add thêm 1 lần bị report vào profile của người chơi bị report
        accused.setNumOfReport(accused.getNumOfReport() + 1);
        accountRepository.save(accused);
        return savedReportOpponentEntity;
    }

    public List<ReportEntity> getReportOfUser(int userId){
        AccountEntity accused = accountServices.findAccountEntityById(userId);
        return reportRepository.findByAccusedIdAndStatus(accused, true);
    }

    public ReportEntity reportField(InputReportFieldDTO inputReportFieldDTO){
        AccountEntity accuser = accountServices.findAccountEntityByIdAndRole(inputReportFieldDTO.getAccuserId(), constant.getUserRole());
        AccountEntity accused = accountServices.findAccountEntityByIdAndRole(inputReportFieldDTO.getAccusedId(), constant.getFieldOwnerRole());
        if(inputReportFieldDTO.getReason().isEmpty()){
            throw new IllegalArgumentException("Report must have reason content!");
        }
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setAccuserId(accuser);
        reportEntity.setAccusedId(accused);
        reportEntity.setReason(inputReportFieldDTO.getReason());
        reportEntity.setStatus(true);

        ReportEntity savedreportEntity = reportRepository.save(reportEntity);
        // add thêm 1 lần bị report vào profile của chủ sân bị report
        accused.setNumOfReport(accused.getNumOfReport() + 1);
        accountRepository.save(accused);
        return savedreportEntity;
    }

    public List<AccountEntity> getAccountOrderByNumOfReport(){
        List<AccountEntity> accountEntityList = accountRepository.findAccountEntitiesByStatusOrderByNumOfReportDesc(true);
        return accountEntityList;
    }
}
