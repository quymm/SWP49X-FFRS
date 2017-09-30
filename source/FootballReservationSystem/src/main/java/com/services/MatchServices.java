package com.services;

import com.dto.OutputMatchDTO;
import com.entity.FriendlyMatchEntity;
import com.entity.TimeSlotEntity;
import com.entity.TourMatchEntity;
import com.repository.FriendlyMatchRepository;
import com.repository.TourMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
@Service
public class MatchServices {
    @Autowired
    FriendlyMatchRepository friendlyMatchRepository;

    @Autowired
    TourMatchRepository tourMatchRepository;

    @Autowired
    TimeSlotServices timeSlotServices;

    public List<OutputMatchDTO> findMatchByFieldIdAndDate(Date targetDate, int fieldId){
        List<TimeSlotEntity> timeSlotEntityList = timeSlotServices.findReserveTimeSlotByFieldIdAndFieldName(targetDate, fieldId);
        List<OutputMatchDTO> outputMatchDTOList = new ArrayList<>();
        for (TimeSlotEntity timeSlot : timeSlotEntityList) {
            FriendlyMatchEntity friendlyMatchEntity = friendlyMatchRepository.findByTimeSlotIdAndStatus(timeSlot, true);
            TourMatchEntity tourMatchEntity = tourMatchRepository.findByTimeSlotIdAndStatus(timeSlot, true);
            if (friendlyMatchEntity != null) {
                outputMatchDTOList.add(convertFromFriendlyMatchEntityToOuputMatchDTO(friendlyMatchEntity));
            }
            if(tourMatchEntity != null){
                outputMatchDTOList.add(convertFromTourMatchToOutputMatchDTO(tourMatchEntity));
            }
        }
        return outputMatchDTOList;
    }

    public OutputMatchDTO convertFromFriendlyMatchEntityToOuputMatchDTO(FriendlyMatchEntity friendlyMatchEntity){
        OutputMatchDTO outputMatchDTO = new OutputMatchDTO();
        outputMatchDTO.setUserId(friendlyMatchEntity.getUserId());
        outputMatchDTO.setWinnerId(0);
        outputMatchDTO.setTimeSlotId(friendlyMatchEntity.getTimeSlotId());
        return outputMatchDTO;
    }

    public OutputMatchDTO convertFromTourMatchToOutputMatchDTO(TourMatchEntity tourMatchEntity){
        OutputMatchDTO outputMatchDTO = new OutputMatchDTO();
        outputMatchDTO.setUserId(tourMatchEntity.getUserId());
        outputMatchDTO.setOpponentId(tourMatchEntity.getOpponentId());
        outputMatchDTO.setTimeSlotId(tourMatchEntity.getTimeSlotId());
        outputMatchDTO.setWinnerId(tourMatchEntity.getWinnerId());
        return  outputMatchDTO;
    }
}
