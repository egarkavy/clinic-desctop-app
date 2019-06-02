package Model.Repositories;

import Model.ClinicDriver;
import Model.Tables.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by super on 5/5/2019.
 */
public class UserRepository {
    private ClinicDriver driver;

    public  UserRepository() throws SQLException {
        driver = new ClinicDriver();
    }

    public List<User> Get(String userName, String password) throws SQLException {
        String sql = String.format("SELECT * from user where userName = '%s' and password = '%s'", userName, password);

        ResultSet results = driver.statement.executeQuery(sql);

        List<User> users = new ArrayList<User>();

        while (results.next()) {
            int id = results.getInt("Id");
            String name = results.getString("UserName");
            String pass = results.getString("Password");
            int roleId = results.getInt("RoleId");

            User user = new User();
            user.setPassword(pass);
            user.setUserName(name);
            user.setRoleId(roleId);
            user.setId(id);

            users.add(user);
        }

        return  users;
    }

    public void Save(String userName, String password) throws SQLException {
        String sql = String.format("INSERT into `user` (`UserName`, `Password`, `RoleId` ) values ('%s', '%s', 2)", userName, password);

        int result = driver.statement.executeUpdate(sql);
    }

    public void Save(String userName, String password, int role) throws SQLException {
        String sql = String.format("INSERT into `user` (`UserName`, `Password`, `RoleId` ) values ('%s', '%s', %s)", userName, password, role);

        int result = driver.statement.executeUpdate(sql);
    }
}
