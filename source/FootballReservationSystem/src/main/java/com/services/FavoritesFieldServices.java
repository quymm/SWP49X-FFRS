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
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        AccountEntity fieldOwner = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
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
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        return favoritesFieldRepository.findByUserIdAndStatus(user, true);
    }

    public List<AccountEntity> findFavoritesFieldListByUserId(int userId) {
        List<FavoritesFieldEntity> favoritesFieldEntityList = findFavoritesFieldByUserId(userId);
        List<AccountEntity> favoritesFieldList = new ArrayList<>();
        if (!favoritesFieldEntityList.isEmpty()) {
            for (FavoritesFieldEntity favoritesFieldEntity : favoritesFieldEntityList) {
                favoritesFieldList.add(favoritesFieldEntity.getFieldOwnerId());
            }
        }
        return favoritesFieldList;
    }

    public FavoritesFieldEntity findFavoritesFieldByUserIdAndFieldOwnerId(int userId, int fieldOwnerId) {
        AccountEntity user = accountServices.findAccountEntityByIdAndRole(userId, constant.getUserRole());
        AccountEntity fieldOwner = accountServices.findAccountEntityByIdAndRole(fieldOwnerId, constant.getFieldOwnerRole());
        return favoritesFieldRepository.findByUserIdAndFieldOwnerIdAndStatus(user, fieldOwner, true);
    }

    public boolean removeFavoritesEntity(int id) {
        FavoritesFieldEntity favoritesFieldEntity = favoritesFieldRepository.findByIdAndStatus(id, true);
        favoritesFieldRepository.delete(favoritesFieldEntity);
        return true;
    }

    public List<AccountEntity> findFavoritesFieldOf2User(int userId1, int userId2) {
        List<FavoritesFieldEntity> favoritesFieldEntityList1 = findFavoritesFieldByUserId(userId1);
        List<FavoritesFieldEntity> favoritesFieldEntityList2 = findFavoritesFieldByUserId(userId2);
        List<AccountEntity> returnFieldOwnerList = new ArrayList<>();
        if (!favoritesFieldEntityList1.isEmpty() && !favoritesFieldEntityList2.isEmpty()) {

            // tìm những sân chung trong sở thích add vào list
            for (FavoritesFieldEntity favoritesFieldEntity1 : favoritesFieldEntityList1) {
                for (FavoritesFieldEntity favoritesFieldEntity2 : favoritesFieldEntityList2) {
                    if (favoritesFieldEntity2.getFieldOwnerId().getId() == favoritesFieldEntity1.getFieldOwnerId().getId()) {
                        returnFieldOwnerList.add(favoritesFieldEntity2.getFieldOwnerId());
                        break;
                    }
                }
            }

            // nếu ko có sân nào chung thì add lần lượt 2 list sở thích của từng người chơi vào thứ tự user 1 đến user 2
            if (returnFieldOwnerList.isEmpty()) {
                for (FavoritesFieldEntity favoritesFieldEntity : favoritesFieldEntityList1) {
                    returnFieldOwnerList.add(favoritesFieldEntity.getFieldOwnerId());
                }
                for (FavoritesFieldEntity favoritesFieldEntity : favoritesFieldEntityList2){
                    returnFieldOwnerList.add(favoritesFieldEntity.getFieldOwnerId());
                }
            } else {
                List<AccountEntity> individualFieldOwnerList = new ArrayList<>();
                favoritesFieldEntityList1.addAll(favoritesFieldEntityList2);
                // nếu đã có sân chung thì tiếp tục add những sân ko phải sân yêu thích chung vào theo thứ tự user 1 đến user 2
                for (FavoritesFieldEntity favoritesFieldEntity : favoritesFieldEntityList1) {
                    boolean check = false;
                    for (AccountEntity accountEntity : returnFieldOwnerList){
                        if(favoritesFieldEntity.getFieldOwnerId().getId() == accountEntity.getId()){
                            check = true;
                        }
                    }
                    if(!check){
                        individualFieldOwnerList.add(favoritesFieldEntity.getFieldOwnerId());
                    }
                }
                returnFieldOwnerList.addAll(individualFieldOwnerList);
            }
        } else if (!favoritesFieldEntityList1.isEmpty()) {
            for (FavoritesFieldEntity favoritesFieldEntity : favoritesFieldEntityList1) {
                returnFieldOwnerList.add(favoritesFieldEntity.getFieldOwnerId());
            }
        } else if (!favoritesFieldEntityList2.isEmpty()) {
            for (FavoritesFieldEntity favoritesFieldEntity : favoritesFieldEntityList2) {
                returnFieldOwnerList.add(favoritesFieldEntity.getFieldOwnerId());
            }
        }
        return returnFieldOwnerList;
    }


}
