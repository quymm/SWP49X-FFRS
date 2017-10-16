package com.dto;

import com.entity.AccountEntity;
import com.entity.TimeSlotEntity;

/**
 * @author MinhQuy
 */
public class MatchReturnDTO {
    private AccountEntity user;

    private AccountEntity opponent;

    private TimeSlotEntity timeSlotEntity;


    public MatchReturnDTO(AccountEntity user, AccountEntity opponent, TimeSlotEntity timeSlotEntity) {
        this.user = user;
        this.opponent = opponent;
        this.timeSlotEntity = timeSlotEntity;
    }

    public MatchReturnDTO() {
    }

    public AccountEntity getUser() {
        return user;
    }

    public void setUser(AccountEntity user) {
        this.user = user;
    }

    public AccountEntity getOpponent() {
        return opponent;
    }

    public void setOpponent(AccountEntity opponent) {
        this.opponent = opponent;
    }

    public TimeSlotEntity getTimeSlotEntity() {
        return timeSlotEntity;
    }

    public void setTimeSlotEntity(TimeSlotEntity timeSlotEntity) {
        this.timeSlotEntity = timeSlotEntity;
    }

}
