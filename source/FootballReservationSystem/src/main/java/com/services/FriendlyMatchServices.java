package com.services;

import com.entity.AccountEntity;
import com.entity.FriendlyMatchEntity;
import com.entity.TimeSlotEntity;
import com.repository.FriendlyMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MinhQuy
 */
@Service
public class FriendlyMatchServices {
    @Autowired
    FriendlyMatchRepository friendlyMatchRepository;

    @Autowired
    AccountServices accountServices;

    public FriendlyMatchEntity createNewFriendlyMatch(TimeSlotEntity timeSlotEntity, int userId){
        AccountEntity userEntity = accountServices.findAccountEntityById(userId, "user");
        FriendlyMatchEntity friendlyMatchEntity = new FriendlyMatchEntity();
        friendlyMatchEntity.setTimeSlotId(timeSlotEntity);
        friendlyMatchEntity.setUserId(userEntity);
        friendlyMatchEntity.setStatus(true);
        return friendlyMatchRepository.save(friendlyMatchEntity);
    }
}
