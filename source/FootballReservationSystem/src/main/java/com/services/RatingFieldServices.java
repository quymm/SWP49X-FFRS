package com.services;

import com.config.Constant;
import com.dto.InputRatingFieldDTO;
import com.entity.AccountEntity;
import com.entity.ProfileEntity;
import com.entity.RatingFieldEntity;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.repository.ProfileRepository;
import com.repository.RatingFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingFieldServices {
    @Autowired
    RatingFieldRepository ratingFieldRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    Constant constant;

    public RatingFieldEntity ratingField(InputRatingFieldDTO ratingFieldDTO) {
        AccountEntity user = accountServices.findAccountEntityById(ratingFieldDTO.getUserId(), constant.getUserRole());
        AccountEntity fieldOwner = accountServices.findAccountEntityById(ratingFieldDTO.getFieldOwnerId(), constant.getFieldOwnerRole());

        RatingFieldEntity ratingFieldEntity = new RatingFieldEntity();
        ratingFieldEntity.setUserId(user);
        ratingFieldEntity.setFieldOwnerId(fieldOwner);
        ratingFieldEntity.setRatingScore(ratingFieldDTO.getRatingScore());
        ratingFieldEntity.setComment(ratingFieldDTO.getComment());
        ratingFieldEntity.setStatus(true);
        RatingFieldEntity savedRatingField = ratingFieldRepository.save(ratingFieldEntity);
        calculateRatingOfFieldOwnerById(fieldOwner.getId());
        return savedRatingField;
    }

    public int calculateRatingOfFieldOwnerById(int fieldOwnerId) {
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, constant.getFieldOwnerRole());
        List<RatingFieldEntity> ratingFieldEntityList = ratingFieldRepository.findByFieldOwnerIdAndStatus(fieldOwner, true);
        if (!ratingFieldEntityList.isEmpty()) {
            // size = 1: chưa có rating nào trước đây, sẽ lấy bằng mức rating đầu
            if (ratingFieldEntityList.size() == 1) {
                fieldOwner.getProfileId().setRatingScore(ratingFieldEntityList.get(0).getRatingScore());
            } else {
                int total = ratingFieldEntityList.get(0).getRatingScore();
                for (int i = 1; i < ratingFieldEntityList.size(); i++) {
                    total = total + ratingFieldEntityList.get(i).getRatingScore();
                }
                fieldOwner.getProfileId().setRatingScore((int) Math.rint(total / ratingFieldEntityList.size()));
            }
            profileRepository.save(fieldOwner.getProfileId());
        }
        return fieldOwner.getProfileId().getRatingScore();
    }
}
