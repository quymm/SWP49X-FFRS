package com.services;

import com.entity.FriendlyMatchEntity;
import com.entity.TeamMemberEntity;
import com.entity.TourMatchEntity;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by truonghuuthanh on 12/9/17.
 */
@Service
public class SmsService {

    public static final String ACCOUNT_SID = "AC81ab80fcf589be3cc266496cd7952ccb";
    public static final String AUTH_TOKEN = "3fc02d6678ccedc1f0d040c9334d0521";
    public static final String PHONETO = "+12013838362";

    public SmsService() {
    }

    public void sendMessageToPlayer(List<TeamMemberEntity> teamA, List<TeamMemberEntity> teamB, TourMatchEntity tourMatchEntity) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        for (int i = 0; i < teamA.size(); i++) {
            String phoneTo = teamA.get(i).getPhone().toString();
            Message
                    .creator(new PhoneNumber(phoneTo), new PhoneNumber(PHONETO),
                            "Đội " +  tourMatchEntity.getUserId().getProfileId().getName().toString()
                                    +
                                    "đã đặt sân  " + tourMatchEntity.getTimeSlotId().getFieldOwnerId().getProfileId().getName().toString()
                                    + " vào ngày " + DateTimeUtils.formatDate(tourMatchEntity.getTimeSlotId().getDate()).toString()
                                    + " lúc " + DateTimeUtils.formatTime(tourMatchEntity.getTimeSlotId().getStartTime()).toString()
                    )
                    .create();
        }
        for (int i = 0; i < teamB.size(); i++) {
            String phoneTo = teamB.get(i).getPhone().toString();
            Message
                    .creator(new PhoneNumber(phoneTo), new PhoneNumber(PHONETO),
                            "Đội " +  tourMatchEntity.getOpponentId().getProfileId().getName().toString()
                            +
                            "đã đặt sân  " + tourMatchEntity.getTimeSlotId().getFieldOwnerId().getProfileId().getName().toString()
                                    + " vào ngày " + DateTimeUtils.formatDate(tourMatchEntity.getTimeSlotId().getDate()).toString()
                                    + " lúc " + DateTimeUtils.formatTime(tourMatchEntity.getTimeSlotId().getStartTime()).toString()

                    )
                    .create();

        }
    }
    public void sendMessageToPlayerFriendLy(List<TeamMemberEntity> teamMemberEntities, FriendlyMatchEntity friendlyMatchEntity){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        for (int i = 0; i < teamMemberEntities.size(); i++) {
            String phoneTo = teamMemberEntities.get(i).getPhone().toString();
            Message
                    .creator(new PhoneNumber(phoneTo), new PhoneNumber(PHONETO),
                            "Đội " + friendlyMatchEntity.getUserId().getProfileId().getName().toString()
                                    + " đã đặt sân ở " + friendlyMatchEntity.getTimeSlotId().getFieldOwnerId().getProfileId().getName().toString()
                                    + " vào ngày " + DateTimeUtils.formatDate(friendlyMatchEntity.getTimeSlotId().getDate()).toString()
                                    + " lúc " + DateTimeUtils.formatTime(friendlyMatchEntity.getTimeSlotId().getStartTime()).toString()
                                    )
                    .create();

        }

    }
}
