package dao;

import models.UserEntity;
import models.UserRoleEntity;

import java.io.Serializable;

/**
 * Created by stark on 19/07/2017.
 */
public class UserRoleDAO extends BaseDAO<UserRoleEntity, String> implements Serializable {
    public UserRoleEntity saveUserRole(UserRoleEntity u){
        return super.save(u);
    }
}
