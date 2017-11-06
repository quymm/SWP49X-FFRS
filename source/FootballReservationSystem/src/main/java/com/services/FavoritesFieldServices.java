package com.services;

import com.config.Constant;
import com.entity.AccountEntity;
import com.entity.FavoritesFieldEntity;
import com.repository.FavoritesFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoritesFieldServices {
    @Autowired
    FavoritesFieldRepository favoritesFieldRepository;

    @Autowired
    AccountServices accountServices;

    @Autowired
    Constant constant;

    public FavoritesFieldEntity createNewFavoritesField(int userId, int fieldOwnerId) {
        AccountEntity user = accountServices.findAccountEntityById(userId, constant.getUserRole());
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, constant.getFieldOwnerRole());
        FavoritesFieldEntity favoritesFieldEntity = favoritesFieldRepository.findByUserIdAndFieldOwnerIdAndStatus(user, fieldOwner, false);
        if (favoritesFieldEntity != null) {
            favoritesFieldEntity.setStatus(true);
        } else {
            favoritesFieldEntity = new FavoritesFieldEntity();
            favoritesFieldEntity.setUserId(user);
            favoritesFieldEntity.setFieldOwnerId(fieldOwner);
            favoritesFieldEntity.setStatus(true);
        }
        return favoritesFieldRepository.save(favoritesFieldEntity);
    }

    public List<FavoritesFieldEntity> findFavoritesFieldByUserId(int userId) {
        AccountEntity user = accountServices.findAccountEntityById(userId, constant.getUserRole());
        return favoritesFieldRepository.findByUserIdAndStatus(user, true);
    }

    public FavoritesFieldEntity findFavoritesFieldByUserIdAndFieldOwnerId(int userId, int fieldOwnerId) {
        AccountEntity user = accountServices.findAccountEntityById(userId, constant.getUserRole());
        AccountEntity fieldOwner = accountServices.findAccountEntityById(fieldOwnerId, constant.getFieldOwnerRole());
        return favoritesFieldRepository.findByUserIdAndFieldOwnerIdAndStatus(user, fieldOwner, true);
    }

    public boolean removeFavoritesEntity(int id) {
        FavoritesFieldEntity favoritesFieldEntity = favoritesFieldRepository.findByIdAndStatus(id, true);
        favoritesFieldEntity.setStatus(false);
        return !favoritesFieldRepository.save(favoritesFieldEntity).getStatus();
    }

    public List<AccountEntity> findFavoritesFieldOf2User(int userId1, int userId2) {
        List<FavoritesFieldEntity> favoritesFieldEntityList1 = findFavoritesFieldByUserId(userId1);
        List<FavoritesFieldEntity> favoritesFieldEntityList2 = findFavoritesFieldByUserId(userId2);
        List<AccountEntity> returnFieldOwnerList = new ArrayList<>();
        if (!favoritesFieldEntityList1.isEmpty() && !favoritesFieldEntityList2.isEmpty()) {

            for (FavoritesFieldEntity favoritesFieldEntity1 : favoritesFieldEntityList1) {
                for (FavoritesFieldEntity favoritesFieldEntity2 : favoritesFieldEntityList2) {
                    if (favoritesFieldEntity2.getFieldOwnerId().getId() == favoritesFieldEntity1.getFieldOwnerId().getId()) {
                        returnFieldOwnerList.add(favoritesFieldEntity2.getFieldOwnerId());
                    }
                }
            }
        }
        return returnFieldOwnerList;
    }


}