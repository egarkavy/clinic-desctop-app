package Model.Repositories;

import Model.ClinicDriver;
import Model.Tables.Doctor;
import Model.Tables.Speciality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by super on 5/5/2019.
 */
public class SpecialityRepository {
    private ClinicDriver driver;

    public SpecialityRepository() throws SQLException {
        driver = new ClinicDriver();
    }

    public List<Speciality> Get() throws SQLException {
        String sql = String.format("SELECT * FROM dental_clinic.speciality;");

        ResultSet results = driver.statement.executeQuery(sql);

        List<Speciality> specialityList = new ArrayList<Speciality>();

        while (results.next()) {
            int id = results.getInt(1);
            String name = results.getString(2);

            Speciality speciality = new Speciality();
            speciality.setId(id);
            speciality.setName(name);

            specialityList.add(speciality);
        }

        return  specialityList;
    }

    public void Save(Speciality speciality) throws SQLException {

        String sql = String.format("INSERT INTO `dental_clinic`.`speciality`\n" +
                "(\n" +
                "`Name`)\n" +
                "VALUES\n" +
                "'%s')", speciality.getName());

        int result = driver.statement.executeUpdate(sql);
    }

    public void Delete(int id) throws SQLException {
        String sql = String.format("delete from dental_clinic.speciality where id = %s", id);

        int result = driver.statement.executeUpdate(sql);
    }

    public void Update(Speciality speciality) throws SQLException {
        String sql = String.format("UPDATE `dental_clinic`.`speciality`\n" +
                "SET\n" +
                "`Name` = '%s'\n" +
                "WHERE `Id` = %s", speciality.getName(), speciality.getId());

        int result = driver.statement.executeUpdate(sql);
    }
}
