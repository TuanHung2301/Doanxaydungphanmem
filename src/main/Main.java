package main;

import controller.AuthController;
import view.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginView view = new LoginView();

            new AuthController(view);
        });
    }
}
   