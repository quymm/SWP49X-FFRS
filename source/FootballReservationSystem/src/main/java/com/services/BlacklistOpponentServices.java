package com.services;

import com.config.Constant;
import com.entity.AccountEntity;
import com.entity.BlacklistOpponentEntity;
import com.repository.BlacklistOpponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlacklistOpponentServices {
    @Autowired
    BlacklistOpponentRepository blacklistOpponentRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    Constant constant;

    public BlacklistOpponentEntity createNewBlackListOpponent(int userId, int opponentId){
        AccountEntity user = accountServices.findAccountEntityById(userId, constant.getUserRole());
        AccountEntity opponent = accountServices.findAccountEntityById(opponentId, constant.getUserRole());

        BlacklistOpponentEntity blacklistOpponentEntity = blacklistOpponentRepository.findByUserIdAndOpponentIdAndStatus(user, opponent, false);
        if(blacklistOpponentEntity != null){
            blacklistOpponentEntity.setStatus(true);
        } else{
            blacklistOpponentEntity = new BlacklistOpponentEntity();
            blacklistOpponentEntity.setUserId(user);
            blacklistOpponentEntity.setOpponentId(opponent);
            blacklistOpponentEntity.setStatus(true);
        }
        return blacklistOpponentRepository.save(blacklistOpponentEntity);
    }

    public List<BlacklistOpponentEntity> findBlacklistByUserId(int userId){
        AccountEntity user = accountServices.findAccountEntityById(userId, constant.getUserRole());
        return blacklistOpponentRepository.findByUserIdAndStatus(user, true);
    }

    public boolean removeBlacklist(int id){
        BlacklistOpponentEntity blacklistOpponentEntity = blacklistOpponentRepository.findByIdAndStatus(id, true);
        blacklistOpponentEntity.setStatus(false);
        return !blacklistOpponentRepository.save(blacklistOpponentEntity).getStatus();
    }

    public BlacklistOpponentEntity findBlacklistByUserIdAndOpponentId(int userId, int opponentId){
        AccountEntity user = accountServices.findAccountEntityById(userId, constant.getUserRole());
        AccountEntity opponent = accountServices.findAccountEntityById(opponentId, constant.getUserRole());
        return blacklistOpponentRepository.findByUserIdAndOpponentIdAndStatus(user, opponent, true);
    }


}
