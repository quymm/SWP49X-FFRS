package service;

import dao.UserDAO;
import models.UserEntity;
import models.UserRoleEntity;
import models.UserRoleEntityPK;
import org.h2.engine.User;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;

/**
 * Created by NGOCHIEU on 2017-05-31.
 */
public class UserService {
    public static UserEntity getUser(String username){
        return JPA.em().find(UserEntity.class, username);
    }
    public static UserEntity getUserWithEM(String username, EntityManager em){
        return em.find(UserEntity.class, username);
    }

    public static boolean updatePassword(String username, String password, String newPassword){
        UserEntity user = getUser(username);
        if(user != null){
            if(user.getPassword().equals(password)){
                user.setPassword(newPassword);
                JPA.em().merge(user);
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public static int saveFirebaseToken(String username, String token) {
        UserDAO.removeDublicateFirebaseToken(token);
        return UserDAO.saveFirebaseToken(username, token);
    }

    public static void addRoleTeacher(String username) {
        UserRoleEntityPK userRoleEntityPK = new UserRoleEntityPK();
        userRoleEntityPK.setUsername(username);
        userRoleEntityPK.setRoleName("teacher");
        UserRoleEntity userRoleEntity = JPA.em().find(UserRoleEntity.class, userRoleEntityPK);
        if (userRoleEntity == null){
            userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRoleName("teacher");
            userRoleEntity.setUsername(username);
            JPA.em().persist(userRoleEntity);
        }

    }
//    public static String findFirebaseToken(String username) {
//        return UserDAO.saveFirebaseToken(username, token);
//    }
}
