package Model.Repositories;

import Model.ClinicDriver;
import Model.Tables.ToDo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by super on 5/5/2019.
 */
public class ToDoRepository {
    private ClinicDriver driver;

    public ToDoRepository() throws SQLException {
        driver = new ClinicDriver();
    }

    public List<ToDo> Get() throws SQLException {
        String sql = String.format("SELECT * from todo");

        ResultSet results = driver.statement.executeQuery(sql);

        List<ToDo> todoList = new ArrayList<ToDo>();

        while (results.next()) {
            int id = results.getInt("Id");
            String text = results.getString("What");
            boolean isDone = results.getBoolean("IsDone");

            ToDo toDo = new ToDo();
            toDo.setId(id);
            toDo.setWhat(text);
            toDo.setDone(isDone);

            todoList.add(toDo);
        }

        return todoList;
    }

    public void Save(ToDo toDo) throws SQLException {
        String sql = String.format("INSERT INTO `dental_clinic`.`todo`\n" +
                "(\n" +
                "`What`,\n" +
                "`IsDone`)\n" +
                "VALUES\n" +
                "(\n" +
                "'%s',\n" +
                "%s);", toDo.getWhat(), toDo.isDone());

        int result = driver.statement.executeUpdate(sql);
    }

    public void Delete(int id) throws SQLException {
        String sql = String.format("delete from `todo` where id = %s", id);

        int result = driver.statement.executeUpdate(sql);
    }

    public void Update(ToDo todo) throws SQLException {
        String sql = String.format("UPDATE `dental_clinic`.`todo`\n" +
                "SET\n" +
                "`What` = '%s',\n" +
                "`IsDone` = %s\n" +
                "WHERE `Id` = %s;\n", todo.getWhat(), todo.isDone(), todo.getId());

        int result = driver.statement.executeUpdate(sql);
    }

}
