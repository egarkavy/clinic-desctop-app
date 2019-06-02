package basic;

import Model.Repositories.NewsRepository;
import Model.Tables.News;
import services.WindowService;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainUser {
    public JPanel panel;
    private JLabel timer;
    private JTextArea newsElement;
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
                WindowService.JustGoToVindow(new Receptions().panel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        yourInfoBtn.addActionListener(e -> {
            try {
                WindowService.JustGoToVindow(new UserInfo().panel);
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
            resultNews = resultNews + news.getCreatedAt() + " " + news.getText() + " \n \n";
        }

        newsElement.setText(resultNews);
    }


}
