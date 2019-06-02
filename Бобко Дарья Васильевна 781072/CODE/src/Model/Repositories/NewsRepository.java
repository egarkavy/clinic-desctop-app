package Model.Repositories;

import Model.ClinicDriver;
import Model.Tables.News;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by super on 5/5/2019.
 */
public class NewsRepository {
    private ClinicDriver driver;

    public NewsRepository() throws SQLException {
        driver = new ClinicDriver();
    }

    public List<News> Get() throws SQLException {
        String sql = String.format("SELECT * from news");

        ResultSet results = driver.statement.executeQuery(sql);

        List<News> newsList = new ArrayList<News>();

        while (results.next()) {
            int id = results.getInt("Id");
            String text = results.getString("Text");
            Date created = results.getDate("CreatedAt");

            News news = new News();
            news.setText(text);
            news.setId(id);
            news.setCreatedAt(created);

            newsList.add(news);
        }

        return  newsList;
    }

    public void Save(String text) throws SQLException {
        String sql = String.format("INSERT into `news` (`Text`) values ('%s')", text);

        int result = driver.statement.executeUpdate(sql);
    }
}
