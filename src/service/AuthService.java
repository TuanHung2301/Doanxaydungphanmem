package service;

import dao.UserDAO;
import model.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        return userDAO.login(username.trim(), password.trim());
    }
}
