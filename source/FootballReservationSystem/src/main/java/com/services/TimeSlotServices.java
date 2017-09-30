package com.services;

import com.entity.FieldEntity;
import com.entity.TimeSlotEntity;
import com.repository.FieldRepository;
import com.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by MinhQuy on 9/29/2017.
 */
@Service
public class TimeSlotServices {
    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    FieldRepository fieldRepository;

    public List<TimeSlotEntity> findReserveTimeSlotByFieldIdAndFieldName(Date targetDate, int fieldId){
        FieldEntity fieldEntity = fieldRepository.findByIdAndStatus(fieldId, true);
        return timeSlotRepository.findByDateAndFieldIdAndReserveStatusAndStatus(targetDate, fieldEntity, true, true);
    }

}
