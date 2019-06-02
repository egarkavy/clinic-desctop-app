package Model.Repositories;

import Model.ClinicDriver;
import Model.Tables.Doctor;
import Model.Tables.Reception;
import Model.Tables.Speciality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by super on 5/5/2019.
 */
public class ReceptionRepository {
    private ClinicDriver driver;
    private UserRepository userRepository;

    public ReceptionRepository() throws SQLException {
        driver = new ClinicDriver();
        userRepository = new UserRepository();
    }

    public List<Reception> Get() throws SQLException {
        String sql = String.format("SELECT * FROM dental_clinic.reception r\n" +
                "join dental_clinic.doctor d on r.doctorid = d.id\n" +
                "join dental_clinic.patient p on r.patientid = p.id");

        ResultSet results = driver.statement.executeQuery(sql);

        List<Reception> receptionList = new ArrayList<Reception>();

        while (results.next()) {
            int id = results.getInt(1);
            java.util.Date date;
            Date datetime = new Date(results.getTimestamp(2).getTime());
            int patientId = results.getInt(3);
            int doctorId = results.getInt(4);
            String details = results.getString(5);
            String doctorName = results.getString(7);
            String patientName = results.getString(15);
            Date patientDOB = results.getDate(16);
            int room = results.getInt(8);

            Reception reception = new Reception(id, datetime, doctorId, patientId, details, patientName, doctorName, room, patientDOB);

            receptionList.add(reception);
        }

        return receptionList;
    }

    public void Save(Reception reception) throws SQLException {
        String sql = String.format("INSERT INTO `dental_clinic`.`reception`\n" +
                "(\n" +
                "`Datetime`,\n" +
                "`PatientId`,\n" +
                "`DoctorId`,\n" +
                "`Details`)\n" +
                "VALUES\n" +
                "(\n" +
                "'%s',\n" +
                "%s,\n" +
                "%s,\n" +
                "'%s');\n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reception.getDateTime()), reception.getPatientId(), reception.getDoctorId(), reception.getDetails());

        int result = driver.statement.executeUpdate(sql);
    }

    public void Delete(int id) throws SQLException {
        String sql = String.format("delete from `reception` where id = %s", id);

        int result = driver.statement.executeUpdate(sql);
    }

    public void Update(Reception reception) throws SQLException {
        String sql = String.format("UPDATE `dental_clinic`.`reception`\n" +
                "SET\n" +
                "`Datetime` = '%s',\n" +
                "`PatientId` = %s,\n" +
                "`DoctorId` = %s,\n" +
                "`Details` = '%s'\n" +
                "WHERE `Id` = %s;\n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reception.getDateTime()), reception.getPatientId(), reception.getDoctorId(), reception.getDetails(), reception.getId());

        int result = driver.statement.executeUpdate(sql);
    }
}
