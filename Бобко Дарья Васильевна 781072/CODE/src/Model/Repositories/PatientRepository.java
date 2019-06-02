package Model.Repositories;

import Model.ClinicDriver;
import Model.Tables.Doctor;
import Model.Tables.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by super on 5/5/2019.
 */
public class PatientRepository {
    private ClinicDriver driver;

    public PatientRepository() throws SQLException {
        driver = new ClinicDriver();
    }

    public List<Patient> Get() throws SQLException {
        String sql = String.format("SELECT * FROM dental_clinic.patient;");

        ResultSet results = driver.statement.executeQuery(sql);

        List<Patient> patients = new ArrayList<Patient>();

        while (results.next()) {
            int id = results.getInt(1);
            String name = results.getString(2);
            Date dob = results.getDate(3);
            String phone = results.getString(4);
            String address = results.getString(5);
            String passport = results.getString(6);
            int userId = results.getInt(7);

            Patient patient = new Patient(id, name, dob, phone, address, passport, userId);

            patients.add(patient);
        }

        return  patients;
    }

    public void Save(Patient patient) throws SQLException {

        String sql = String.format("INSERT INTO `dental_clinic`.`patient`\n" +
                "(\n" +
                "`Name`,\n" +
                "`DOB`,\n" +
                "`Phone`,\n" +
                "`Adress`,\n" +
                "`PassportNumber`,\n" +
                "`UserId`) \n" +
                "VALUES (\n" +
                "'%s',\n" +
                "'%s',\n" +
                "'%s',\n" +
                "'%s',\n" +
                "'%s',\n" +
                "%s)", patient.getName(), patient.getDOB(), patient.getPhone(), patient.getAddress(), patient.getPassportNumber(), patient.getUserId());

        int result = driver.statement.executeUpdate(sql);
    }

    public void Save(int userId) throws SQLException {
        String sql = String.format("INSERT INTO `dental_clinic`.`patient`\n" +
                "(\n" +
                "`UserId`) \n" +
                "VALUES (\n" +
                "%s)", userId);

        int result = driver.statement.executeUpdate(sql);
    }

    public void Delete(int id) throws SQLException {
        String sql = String.format("delete from `patient` where id = %s", id);

        int result = driver.statement.executeUpdate(sql);
    }

    public void Update(Patient patient) throws SQLException {
        String sql = String.format("UPDATE `dental_clinic`.`patient`\n" +
                "SET\n" +
                "`Name` = '%s',\n" +
                "`DOB` = '%s',\n" +
                "`Phone` = '%s',\n" +
                "`Adress` = '%s',\n" +
                "`PassportNumber` = '%s'\n" +
                "WHERE `Id` = %s;\n", patient.getName(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(patient.getDOB()), patient.getPhone(), patient.getAddress(), patient.getPassportNumber(), patient.getId());

        int result = driver.statement.executeUpdate(sql);
    }
}
