<<<<<<< HEAD
package com.services;

import com.dto.InputMatchingRequestDTO;
import com.dto.OutputMatchDTO;
import com.entity.*;
import com.repository.FieldRepository;
import com.repository.FriendlyMatchRepository;
import com.repository.MatchingRequestRepository;
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
    MatchingRequestRepository matchingRequestRepository;

    @Autowired
    TimeSlotServices timeSlotServices;

    @Autowired
    FieldServices fieldServices;

    @Autowired
    AccountServices accountServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    public List<OutputMatchDTO> findMatchByFieldIdAndDate(Date targetDate, int fieldId) {
        List<TimeSlotEntity> timeSlotEntityList = timeSlotServices.findReserveTimeSlotByFieldIdAndFieldName(targetDate, fieldId);
        List<OutputMatchDTO> outputMatchDTOList = new ArrayList<>();
        for (TimeSlotEntity timeSlot : timeSlotEntityList) {
            FriendlyMatchEntity friendlyMatchEntity = friendlyMatchRepository.findByTimeSlotIdAndStatus(timeSlot, true);
            TourMatchEntity tourMatchEntity = tourMatchRepository.findByTimeSlotIdAndStatus(timeSlot, true);
            if (friendlyMatchEntity != null) {
                outputMatchDTOList.add(convertFromFriendlyMatchEntityToOuputMatchDTO(friendlyMatchEntity));
            }
            if (tourMatchEntity != null) {
                outputMatchDTOList.add(convertFromTourMatchToOutputMatchDTO(tourMatchEntity));
            }
        }
        return outputMatchDTOList;
    }

    public List<OutputMatchDTO> findMatchByFieldOwnerIdAndDate(Date targetDate, int fieldOwnerId) {

        List<FieldEntity> fieldEntityList = fieldServices.findFieldEntityByFieldOwnerId(fieldOwnerId);
        List<OutputMatchDTO> outputMatchDTOList = new ArrayList<>();
        for (FieldEntity fieldEntity : fieldEntityList) {
            List<OutputMatchDTO> outputMatchDTOListWithField = findMatchByFieldIdAndDate(targetDate, fieldEntity.getId());
            outputMatchDTOList.addAll(outputMatchDTOListWithField);
        }
        return outputMatchDTOList;
    }

    public MatchingRequestEntity createNewMatchingRequest(InputMatchingRequestDTO inputMatchingRequestDTO) {
        MatchingRequestEntity matchingRequestEntity = convertFromInputMatchingRequestDTOToEntity(inputMatchingRequestDTO);
        return matchingRequestRepository.save(matchingRequestEntity);
    }

    public OutputMatchDTO convertFromFriendlyMatchEntityToOuputMatchDTO(FriendlyMatchEntity friendlyMatchEntity) {
        OutputMatchDTO outputMatchDTO = new OutputMatchDTO();
        outputMatchDTO.setUserId(friendlyMatchEntity.getUserId());
        outputMatchDTO.setOpponentId(friendlyMatchEntity.getUserId());
        outputMatchDTO.setWinnerId(0);
        outputMatchDTO.setTimeSlotId(friendlyMatchEntity.getTimeSlotId());
        return outputMatchDTO;
    }

    public OutputMatchDTO convertFromTourMatchToOutputMatchDTO(TourMatchEntity tourMatchEntity) {
        OutputMatchDTO outputMatchDTO = new OutputMatchDTO();
        outputMatchDTO.setUserId(tourMatchEntity.getUserId());
        outputMatchDTO.setOpponentId(tourMatchEntity.getOpponentId());
        outputMatchDTO.setTimeSlotId(tourMatchEntity.getTimeSlotId());
        outputMatchDTO.setWinnerId(tourMatchEntity.getWinnerId());
        return outputMatchDTO;
    }

    public MatchingRequestEntity convertFromInputMatchingRequestDTOToEntity(InputMatchingRequestDTO inputMatchingRequestDTO) {
        MatchingRequestEntity matchingRequestEntity = new MatchingRequestEntity();
        AccountEntity userEntity = accountServices.findAccountEntityById(inputMatchingRequestDTO.getUserId());
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findFieldTypeEntityById(inputMatchingRequestDTO.getFieldTypeId());
        matchingRequestEntity.setUserId(userEntity);
        matchingRequestEntity.setFieldTypeId(fieldTypeEntity);
        matchingRequestEntity.setLongitude(inputMatchingRequestDTO.getLongitude());
        matchingRequestEntity.setLatitude(inputMatchingRequestDTO.getLatitude());
        matchingRequestEntity.setDuration(inputMatchingRequestDTO.getDuration());
        matchingRequestEntity.setStartTime(inputMatchingRequestDTO.getStartTime());
        matchingRequestEntity.setStatus(true);
        return matchingRequestEntity;
    }
}
=======
package com.services;

import com.dto.InputMatchingRequestDTO;
import com.dto.OutputMatchDTO;
import com.entity.*;
import com.repository.FriendlyMatchRepository;
import com.repository.MatchingRequestRepository;
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
    MatchingRequestRepository matchingRequestRepository;

    @Autowired
    TimeSlotServices timeSlotServices;

    @Autowired
    FieldServices fieldServices;

    @Autowired
    AccountServices accountServices;

    @Autowired
    FieldTypeServices fieldTypeServices;

    public List<OutputMatchDTO> findMatchByFieldIdAndDate(Date targetDate, int fieldId) {
        List<TimeSlotEntity> timeSlotEntityList = timeSlotServices.findTimeSlotByDateFieldIdAndReservateStatus(targetDate, fieldId, true);
        List<OutputMatchDTO> outputMatchDTOList = new ArrayList<>();
        for (TimeSlotEntity timeSlot : timeSlotEntityList) {
            FriendlyMatchEntity friendlyMatchEntity = friendlyMatchRepository.findByTimeSlotIdAndStatus(timeSlot, true);
            TourMatchEntity tourMatchEntity = tourMatchRepository.findByTimeSlotIdAndStatus(timeSlot, true);
            if (friendlyMatchEntity != null) {
                outputMatchDTOList.add(convertFromFriendlyMatchEntityToOuputMatchDTO(friendlyMatchEntity));
            }
            if (tourMatchEntity != null) {
                outputMatchDTOList.add(convertFromTourMatchToOutputMatchDTO(tourMatchEntity));
            }
        }
        return outputMatchDTOList;
    }

    public List<OutputMatchDTO> findMatchByFieldOwnerIdAndDate(Date targetDate, int fieldOwnerId) {

        List<FieldEntity> fieldEntityList = fieldServices.findFieldEntityByFieldOwnerId(fieldOwnerId);
        List<OutputMatchDTO> outputMatchDTOList = new ArrayList<>();
        for (FieldEntity fieldEntity : fieldEntityList) {
            List<OutputMatchDTO> outputMatchDTOListWithField = findMatchByFieldIdAndDate(targetDate, fieldEntity.getId());
            outputMatchDTOList.addAll(outputMatchDTOListWithField);
        }
        return outputMatchDTOList;
    }

    public MatchingRequestEntity createNewMatchingRequest(InputMatchingRequestDTO inputMatchingRequestDTO) {
        MatchingRequestEntity matchingRequestEntity = convertFromInputMatchingRequestDTOToEntity(inputMatchingRequestDTO);
        return matchingRequestRepository.save(matchingRequestEntity);
    }

    public OutputMatchDTO convertFromFriendlyMatchEntityToOuputMatchDTO(FriendlyMatchEntity friendlyMatchEntity) {
        OutputMatchDTO outputMatchDTO = new OutputMatchDTO();
        outputMatchDTO.setUserId(friendlyMatchEntity.getUserId());
        outputMatchDTO.setOpponentId(friendlyMatchEntity.getUserId());
        outputMatchDTO.setWinnerId(0);
        outputMatchDTO.setTimeSlotId(friendlyMatchEntity.getTimeSlotId());
        return outputMatchDTO;
    }

    public OutputMatchDTO convertFromTourMatchToOutputMatchDTO(TourMatchEntity tourMatchEntity) {
        OutputMatchDTO outputMatchDTO = new OutputMatchDTO();
        outputMatchDTO.setUserId(tourMatchEntity.getUserId());
        outputMatchDTO.setOpponentId(tourMatchEntity.getOpponentId());
        outputMatchDTO.setTimeSlotId(tourMatchEntity.getTimeSlotId());
        outputMatchDTO.setWinnerId(tourMatchEntity.getWinnerId());
        return outputMatchDTO;
    }

    public MatchingRequestEntity convertFromInputMatchingRequestDTOToEntity(InputMatchingRequestDTO inputMatchingRequestDTO) {
        MatchingRequestEntity matchingRequestEntity = new MatchingRequestEntity();
        AccountEntity userEntity = accountServices.findAccountEntityById(inputMatchingRequestDTO.getUserId());
        FieldTypeEntity fieldTypeEntity = fieldTypeServices.findFieldTypeEntityById(inputMatchingRequestDTO.getFieldTypeId());
        matchingRequestEntity.setUserId(userEntity);
        matchingRequestEntity.setFieldTypeId(fieldTypeEntity);
        matchingRequestEntity.setLongitude(inputMatchingRequestDTO.getLongitude());
        matchingRequestEntity.setLatitude(inputMatchingRequestDTO.getLatitude());
        matchingRequestEntity.setDuration(inputMatchingRequestDTO.getDuration());
        matchingRequestEntity.setStartTime(inputMatchingRequestDTO.getStartTime());
        matchingRequestEntity.setStatus(true);
        return matchingRequestEntity;
    }
}
>>>>>>> master
