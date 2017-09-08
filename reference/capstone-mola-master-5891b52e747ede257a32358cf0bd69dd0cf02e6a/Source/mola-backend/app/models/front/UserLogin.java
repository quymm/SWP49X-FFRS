package models.front;

import models.UserEntity;
import service.StaffService;


public class UserLogin {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserLogin() {
    }

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String validate(){
        if (username == null || username.isEmpty()){
            return "Please enter username";
        }
        if (password == null || password.isEmpty()){
            return "Please enter password";
        }
        UserEntity staff = StaffService.checkLogin(this);
        if (staff == null){
            return "Invalid username or password";
        }
        return null;
    }
}
