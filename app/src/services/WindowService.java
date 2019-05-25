package services;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by super on 5/12/2019.
 */
public class WindowService {
    public interface OnAppTimer {
        void OnTimer(int mins, int secs);
    }

    private static List<OnAppTimer> subscribers;
    private static long timeInApp = 0;

    static {
        subscribers = new ArrayList<OnAppTimer>();

        new Timer(1000, e -> {
            timeInApp++;

            int mins = (int)(timeInApp / 60);
            int secs = (int)(timeInApp - mins * 60);

            for (OnAppTimer sub: subscribers) {
                sub.OnTimer(mins, secs);
            }
        }).start();
    }

    public static void GoToWindow(JPanel toClose, JPanel toOpen) {
        JFrame frame = new JFrame("MainAdmin");
        frame.setContentPane(toOpen);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        JFrame topFrame = (JFrame) SwingUtilities.getRoot(toClose);
        topFrame.dispatchEvent(new WindowEvent(topFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static void JustGoToVindow(JPanel toOpen) {
        JFrame frame = new JFrame("MainAdmin");
        frame.setContentPane(toOpen);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void SubscribeOnAppTimer(OnAppTimer listener) {
        subscribers.add(listener);
    }
}
