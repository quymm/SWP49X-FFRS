<<<<<<< HEAD
package com.dto;

import com.entity.AccountEntity;
import com.entity.TimeSlotEntity;

/**
 * Created by MinhQuy on 9/30/2017.
 */
public class OutputMatchDTO {
    private AccountEntity userId;

    private AccountEntity opponentId;

    private TimeSlotEntity timeSlotId;

    private Integer winnerId;

    public OutputMatchDTO(AccountEntity userId, AccountEntity opponentId, TimeSlotEntity timeSlotId, Integer winnerId) {
        this.userId = userId;
        this.opponentId = opponentId;
        this.timeSlotId = timeSlotId;
        this.winnerId = winnerId;
    }

    public OutputMatchDTO() {
    }

    public AccountEntity getUserId() {
        return userId;
    }

    public void setUserId(AccountEntity userId) {
        this.userId = userId;
    }

    public AccountEntity getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(AccountEntity opponentId) {
        this.opponentId = opponentId;
    }

    public TimeSlotEntity getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(TimeSlotEntity timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }
}
=======
package com.dto;

import com.entity.AccountEntity;
import com.entity.TimeSlotEntity;

/**
 * Created by MinhQuy on 9/30/2017.
 */
public class OutputMatchDTO {
    private AccountEntity userId;

    private AccountEntity opponentId;

    private TimeSlotEntity timeSlotId;

    private Integer winnerId;

    public OutputMatchDTO(AccountEntity userId, AccountEntity opponentId, TimeSlotEntity timeSlotId, Integer winnerId) {
        this.userId = userId;
        this.opponentId = opponentId;
        this.timeSlotId = timeSlotId;
        this.winnerId = winnerId;
    }

    public OutputMatchDTO() {
    }

    public AccountEntity getUserId() {
        return userId;
    }

    public void setUserId(AccountEntity userId) {
        this.userId = userId;
    }

    public AccountEntity getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(AccountEntity opponentId) {
        this.opponentId = opponentId;
    }

    public TimeSlotEntity getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(TimeSlotEntity timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }
}
>>>>>>> master
