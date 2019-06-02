import javax.swing.*;

import basic.*;

import java.sql.SQLException;

/**
 * Created by super on 4/28/2019.
 */
public class Program {
    public static void main(String args[]) throws SQLException {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Login().panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
