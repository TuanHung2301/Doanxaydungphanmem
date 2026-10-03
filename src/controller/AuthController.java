package controller;

import DB.Session;
import model.User;
import service.UserService;
import view.LoginView;
import view.MainView;

public class AuthController {

    private LoginView view;
    private UserService userService;

    public AuthController(LoginView view) {
        this.view = view;
        this.userService = new UserService();
        initEvents();
        this.view.setVisible(true);
    }

    private void initEvents() {
        view.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("️Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu!");
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            Session.currentUser = user;
            view.dispose();
            new MainController(new MainView());
        } else {
            view.showMessage("Sai Tên đăng nhập hoặc Mật khẩu. Vui lòng thử lại!");
        }
    }
}
