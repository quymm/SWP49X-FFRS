package dao;


import models.TeacherEntity;
import utils.Const;
import models.UserEntity;
import models.UserRoleEntity;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public class UserDAO extends BaseDAO<UserEntity, Integer> implements Serializable{
    public static UserEntity getUser(String username){
        return JPA.em().find(UserEntity.class, username);
    }
    public static UserEntity getUser(EntityManager em,String username){
        return em.find(UserEntity.class, username);
    }
    public static UserEntity merge(UserEntity user) { return JPA.em().merge(user); }
    public static UserEntity merge(EntityManager em, UserEntity user) { return em.merge(user); }
    public static List<UserEntity> getAllStaff(){
        return JPA.em().createQuery("SELECT u FROM UserEntity u, UserRoleEntity r " +
                                                "WHERE u.username = r.username " +
                                                "AND r.roleName = :role")
                .setParameter("role", Const.ROLE_STAFF)
                .getResultList()
                ;
    }


    public static UserEntity addStaff(UserEntity staff){

        UserEntity user = JPA.em().merge(staff);

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUsername(staff.getUsername());
        userRoleEntity.setRoleName(Const.ROLE_STAFF);
        JPA.em().persist(userRoleEntity);

        return user;
    }

    public static UserEntity register(UserEntity newUser) {
        JPA.em().persist(newUser);

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUsername(newUser.getUsername());
        userRoleEntity.setRoleName(Const.ROLE_LEARNER);
        JPA.em().persist(userRoleEntity);

        return newUser;
    }
    public static UserEntity find(Integer id){
        return JPA.em().find(UserEntity.class, id);
    }

    public static UserEntity updateAvatar(String username, String avatar) {
        UserEntity userEntity = JPA.em().find(UserEntity.class, username);
        userEntity.setAvatar(avatar);

        JPA.em().merge(userEntity);

        return userEntity;
    }

    public static UserEntity updateUser(UserEntity userEntity) {
        return JPA.em().merge(userEntity);
    }

    public static boolean isTeacher(String username) {
        List result = JPA.em().createQuery("SELECT u FROM UserRoleEntity u " +
                "WHERE u.username = :username " +
                "AND u.roleName = :role")
                .setParameter("username", username)
                .setParameter("role", Const.ROLE_TEACHER)
                .getResultList();
        return !result.isEmpty();
    }

    public static int saveFirebaseToken(String username, String token) {
        return JPA.em().createQuery("UPDATE UserEntity u SET u.firebaseToken = :token WHERE u.username = :username")
                .setParameter("username", username)
                .setParameter("token", token)
                .executeUpdate();
    }
    public static void removeDublicateFirebaseToken(String token){
        JPA.em().createQuery("UPDATE UserEntity u SET u.firebaseToken = '' WHERE u.firebaseToken = :token")
                .setParameter("token", token)
                .executeUpdate();
    }

    public static List<UserEntity> getAllLearner() {
        return JPA.em().createQuery("SELECT u FROM UserEntity u, UserRoleEntity r " +
                "WHERE u.username = r.username " +
                "AND r.roleName = :role")
                .setParameter("role", Const.ROLE_LEARNER)
                .getResultList()
                ;
    }

    public static List<UserEntity> getAllLearner(EntityManager em) {
        return em.createQuery("SELECT u FROM UserEntity u, UserRoleEntity r " +
                "WHERE u.username = r.username " +
                "AND r.roleName = :role")
                .setParameter("role", Const.ROLE_LEARNER)
                .getResultList()
                ;
    }
}
