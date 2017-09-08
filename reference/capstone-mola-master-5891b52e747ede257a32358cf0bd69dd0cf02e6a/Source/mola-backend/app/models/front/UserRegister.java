package models.front;

import dao.UserDAO;
import models.UserEntity;

/**
 * Created by rocks on 6/1/2017.
 */
public class UserRegister {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public UserRegister() {
    }

    public UserRegister(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String validate() {
        if (username == null || username.isEmpty()) {
            return "Username is required";
        }
        if (username.length() < 6) {
            return "Username must have at least 6 characters";
        }
        UserEntity user = UserDAO.getUser(username);
        if (user != null) {
            return "This username has already existed";
        }
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        if (password.length() < 6) {
            return "Password must have at least 6 characters";
        }
        if (email == null || !email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+" +
                "(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            return "Invalid email address";
        }
        if (firstName == null || firstName.isEmpty()) {
            return "First Name is required";
        }
        if (lastName == null || lastName.isEmpty()) {
            return "Last Name is required";
        }
        return null;
    }
}
