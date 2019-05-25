package basic;

import Model.Repositories.NewsRepository;
import Model.Tables.News;
import services.WindowService;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by super on 5/12/2019.
 */
public class MainUser {
    public JPanel panel;
    private JLabel timer;
    private JTextArea newsElement;
    private JButton button1;
    private JButton teamsBtn;
    private JButton yourReceptionsBtn;
    private JButton yourInfoBtn;

    private NewsRepository newsRepository;

    public MainUser() throws SQLException {
        newsRepository = new NewsRepository();

        WindowService.SubscribeOnAppTimer((m, s) -> {
            timer.setText(m + " : " + s);
        });

        FillNews();

        yourReceptionsBtn.addActionListener(e -> {
            try {
                WindowService.JustGoToVindow(new Doctors().panel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        teamsBtn.addActionListener(e -> {
            try {
                WindowService.JustGoToVindow(new Teams().panel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        yourInfoBtn.addActionListener(e -> {
            try {
                WindowService.JustGoToVindow(new Games().panel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void FillNews() throws SQLException {
        ArrayList<News> news = (ArrayList<News>) newsRepository.Get();

        GenerateNewsElements(news);
    }

    private void GenerateNewsElements(List<News> newsList) {
        String resultNews = "";
        for(News news : newsList) {
//            JPanel oneNews = GenerateOneNewsElement(news);
            resultNews = resultNews + news.getCreatedAt() + " " + news.getText() + " \n \n";
//            newsPanel.add(oneNews, new GridConstraints());
        }

        newsElement.setText(resultNews);

//        newsPanel.revalidate();
//        newsPanel.repaint();
    }

    private JPanel GenerateOneNewsElement(News news) {
        JPanel wrapper = new JPanel();

        JLabel timestamp = new JLabel();
        timestamp.setText(news.getCreatedAt().toString());

        JLabel newsText = new JLabel();
        newsText.setText(news.getText());

        wrapper.add(timestamp);
        wrapper.add(newsText);

        return wrapper;
    }

}
