package com.services;

import com.config.Constant;
import com.dto.InputRatingFieldDTO;
import com.entity.AccountEntity;
import com.entity.RatingFieldEntity;
import com.repository.RatingFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingFieldServices {
    @Autowired
    RatingFieldRepository ratingFieldRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    Constant constant;
    public RatingFieldEntity ratingField(InputRatingFieldDTO ratingFieldDTO){
        AccountEntity user = accountServices.findAccountEntityById(ratingFieldDTO.getUserId(), constant.getUserRole());
        AccountEntity fieldOwner = accountServices.findAccountEntityById(ratingFieldDTO.getFieldOwnerId(), constant.getFieldOwnerRole());

        RatingFieldEntity ratingFieldEntity = new RatingFieldEntity();
        ratingFieldEntity.setUserId(user);
        ratingFieldEntity.setFieldOwnerId(fieldOwner);
        ratingFieldEntity.setRatingScore(ratingFieldDTO.getRatingScore());
        ratingFieldEntity.setComment(ratingFieldDTO.getComment());
        ratingFieldEntity.setStatus(true);
        return ratingFieldRepository.save(ratingFieldEntity);
    }
}
