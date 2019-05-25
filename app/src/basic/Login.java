package basic;

import Model.Repositories.UserRepository;
import Model.Tables.User;
import services.UserService;
import services.WindowService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by super on 4/28/2019.
 */
public class Login{
    private JButton login;
    public JPanel panel;
    private JTextField loginInput;
    private JTextField passwordInput;
    private JButton register;

    private UserRepository userRepository;
    private WindowService windowService;

    public Login() throws SQLException {
        userRepository = new UserRepository();
        windowService = new WindowService();

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginInput.getText();
                String pass = passwordInput.getText();

                try {
                    User user = userRepository.Get(login, pass).get(0);

                    if (user != null) {
                        UserService.currentUser = user;
                        GoToMainWindow(user);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                //compare with database
            }
        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginInput.getText();
                String pass = passwordInput.getText();

                try {
                    userRepository.Save(login, pass);

                    JOptionPane.showMessageDialog(null, "Registered", "InfoBox:", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void GoToMainWindow(User user) throws SQLException {
        if (user.getRoleId() == 1) {
            WindowService.GoToWindow(panel, new MainAdmin().panel);
        }
        else {
            WindowService.GoToWindow(panel, new MainUser().panel);
        }

    }
}
