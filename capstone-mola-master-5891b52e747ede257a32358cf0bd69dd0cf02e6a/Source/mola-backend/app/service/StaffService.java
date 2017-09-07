package service;


import dao.LanguageTeachDAO;
import dao.UserDAO;
import models.UserEntity;
import models.front.BStaff;
import models.front.BStaffUpdate;
import models.front.UserLogin;
import play.db.jpa.JPA;

import java.util.List;

public class StaffService {
    public static UserEntity checkLogin(UserLogin userLogin){

        UserEntity user = UserDAO.getUser(userLogin.getUsername());

        if ( user == null || !user.getPassword().equals(user.getPassword())){
            return null;
        }
        return user;
    }

    public static List<UserEntity> getAllStaff(){
        return UserDAO.getAllStaff();
    }



    public static void addStaff(BStaff bUser){
        UserEntity user  =new UserEntity();
        user.setUsername(bUser.getUsername());
        user.setEmail(bUser.getEmail());
        user.setFirstName(bUser.getFirstname());
        user.setPassword(bUser.getPassword());
        user.setLastName(bUser.getLastname());

        UserDAO.addStaff(user);
    }

    public static void updateStaff(BStaffUpdate UserUpdate){
        int id = Integer.parseInt(UserUpdate.getId());
        UserEntity User = JPA.em().find(UserEntity.class, id);
        User.setUsername(UserUpdate.getUsername());
        User.setEmail(UserUpdate.getEmail());
        User.setFirstName(UserUpdate.getFirstname());
        User.setLastName(UserUpdate.getLastname());
        JPA.em().merge(User);
    }

    public static void deleteStaff(String username){
        UserEntity user = new UserEntity();
        user.setUsername(username);
        JPA.em().remove(JPA.em().find(UserEntity.class, username));
    }

    public static boolean acceptLanguageTeachRequest(String teacherId, String language){
        return LanguageTeachDAO.acceptLanguageTeachRequest(Integer.parseInt(teacherId), language);
    }
}
