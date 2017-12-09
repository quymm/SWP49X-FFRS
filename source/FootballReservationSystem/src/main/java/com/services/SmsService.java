package com.services;

import com.entity.FriendlyMatchEntity;
import com.entity.TeamMemberEntity;
import com.entity.TourMatchEntity;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.utils.DateTimeUtils;

import java.util.List;

/**
 * Created by truonghuuthanh on 12/9/17.
 */
public class SmsService {
    public static final String ACCOUNT_SID = "ACf15257b6928353c02306b906f2f6b6fb";
    public static final String AUTH_TOKEN = "805535a2fac3fd5f37d7e3be05688078";
    public static final String PHONETO = "+19032744531";

    public SmsService() {
    }

    public static void sendMessageToPlayer(List<TeamMemberEntity> teamA, List<TeamMemberEntity> teamB, TourMatchEntity tourMatchEntity) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        for (int i = 0; i < teamA.size(); i++) {
            String phoneTo = teamA.get(i).getPhone().toString();
            Message
                    .creator(new PhoneNumber(phoneTo), new PhoneNumber(PHONETO),
                            "Đến sân " + tourMatchEntity.getTimeSlotId().getFieldOwnerId().getProfileId().getName()
                                    + " vào ngày " + DateTimeUtils.formatDate(tourMatchEntity.getTimeSlotId().getDate()).toString()
                                    + " lúc " + DateTimeUtils.formatTime(tourMatchEntity.getTimeSlotId().getStartTime()).toString()
                                    + " để tham gia cùng " + tourMatchEntity.getUserId().getProfileId().getName().toString())
                    .create();
        }
        for (int i = 0; i < teamB.size(); i++) {
            String phoneTo = teamB.get(i).getPhone().toString();
            Message
                    .creator(new PhoneNumber(phoneTo), new PhoneNumber(PHONETO),
                            "Đến sân " + tourMatchEntity.getTimeSlotId().getFieldOwnerId().getProfileId().getName().toString()
                                    + " vào ngày " + DateTimeUtils.formatDate(tourMatchEntity.getTimeSlotId().getDate()).toString()
                                    + " lúc " + DateTimeUtils.formatTime(tourMatchEntity.getTimeSlotId().getStartTime()).toString()
                                    + " để tham gia cùng " + tourMatchEntity.getOpponentId().getProfileId().getName().toString())
                    .create();

        }
    }
    public static void sendMessageToPlayer(List<TeamMemberEntity> teamMemberEntities, FriendlyMatchEntity friendlyMatchEntity){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        for (int i = 0; i < teamMemberEntities.size(); i++) {
            String phoneTo = teamMemberEntities.get(i).getPhone().toString();
            Message
                    .creator(new PhoneNumber(phoneTo), new PhoneNumber(PHONETO),
                            "Đến sân " + friendlyMatchEntity.getTimeSlotId().getFieldOwnerId().getProfileId().getName().toString()
                                    + " vào  " + DateTimeUtils.formatDate(friendlyMatchEntity.getTimeSlotId().getDate()).toString()
                                    + " lúc " + DateTimeUtils.formatTime(friendlyMatchEntity.getTimeSlotId().getStartTime()).toString()
                                    + " để tham gia cùng " + friendlyMatchEntity.getUserId().getProfileId().getName().toString())
                    .create();

        }

    }
}
