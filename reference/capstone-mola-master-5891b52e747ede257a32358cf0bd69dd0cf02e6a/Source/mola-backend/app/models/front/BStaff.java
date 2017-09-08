package models.front;

import models.UserEntity;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import service.StaffService;

/**
 * Created by NGOCHIEU on 2017-05-26.
 */
public class BStaff {
    private String email;
    private String username;
    private String password;
    private String firstname;
    private String lastname;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String validate(){
        if (username == null){
            return "Username is required";
        }
        if (username.length() < 6){
            return "Username must have at least 6 characters";
        }
        UserEntity staff = JPA.em().find(UserEntity.class, this.username);
        if (staff != null){
            return "This username has already existed";
        }
        if (password == null){
            return "Password is required";
        }
        if (password.length() < 6){
            return "Password must have at least 6 characters";
        }
        if (!email.matches("\\A[^@]+@[^@]+\\z")){
            return "Invalid email";
        }


        return null;
    }
}
