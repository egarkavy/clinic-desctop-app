package Model.Repositories;

import Model.ClinicDriver;
import Model.Tables.Doctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by super on 5/5/2019.
 */
public class DoctorsRepository {
    private ClinicDriver driver;
    private UserRepository userRepository;

    public DoctorsRepository() throws SQLException {
        driver = new ClinicDriver();
        userRepository = new UserRepository();
    }

    public List<Doctor> Get() throws SQLException {
        String sql = String.format("SELECT * \n" +
                                "FROM dental_clinic.doctor d\n" +
                                "JOIN dental_clinic.speciality s on d.specialityId = s.id \n" +
                                "join dental_clinic.user u on d.userid = u.id");

        ResultSet results = driver.statement.executeQuery(sql);

        List<Doctor> doctorList = new ArrayList<Doctor>();

        while (results.next()) {
            int id = results.getInt(1);
            String name = results.getString(2);
            int room = results.getInt(3);
            String phone = results.getString(4);
            Date start = results.getTime(5);
            Date end = results.getTime(6);
            int sPid = results.getInt(7);
            String sName = results.getString(10);
            int userId = results.getInt(8);
            String userLogin = results.getString(12);
            String password = results.getString(13);

            Doctor doctor = new Doctor();
            doctor.setId(id);
            doctor.setName(name);
            doctor.setRoom(room);
            doctor.setPhone(phone);
            doctor.setStartTime(start);
            doctor.setEndTime(end);
            doctor.setSpecialityId(sPid);
            doctor.setSpecialityName(sName);
            doctor.setUserId(userId);
            doctor.setLogin(userLogin);
            doctor.setPassword(password);

            doctorList.add(doctor);
        }

        return  doctorList;
    }

    public void Save(Doctor doctor) throws SQLException {
        userRepository.Save(doctor.getLogin(), doctor.getPassword(), 3);
        int userId = userRepository.Get(doctor.getLogin(), doctor.getPassword()).stream().findFirst().get().getId();

        String sql = String.format("INSERT INTO `dental_clinic`.`doctor`\n" +
                "(`Name`,\n" +
                "`Room`,\n" +
                "`Phone`,\n" +
                "`StartTime`,\n" +
                "`EndTime`,\n" +
                "`SpecialityId`," +
                "`UserId`) \n" +
                "VALUES\n" +
                "(\n" +
                "'%s',\n" +
                "%s,\n" +
                "'%s',\n" +
                "'%s',\n" +
                "'%s',\n" +
                "%s,\n" +
                "%s" +
                ");\n", doctor.getName(), doctor.getRoom(), doctor.getPhone(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(doctor.getStartTime()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(doctor.getEndTime()), doctor.getSpecialityId(), userId);

        int result = driver.statement.executeUpdate(sql);


    }

    public void Delete(int id) throws SQLException {
        String sql = String.format("delete from `doctor` where id = %s", id);

        int result = driver.statement.executeUpdate(sql);
    }

    public void Update(Doctor doctor) throws SQLException {
        String sql = String.format("UPDATE `dental_clinic`.`doctor`\n" +
                "SET\n" +
                "`Name` = '%s',\n" +
                "`Room` = %s,\n" +
                "`Phone` = '%s',\n" +
                "`StartTime` = '%s',\n" +
                "`EndTime` = '%s',\n" +
                "`SpecialityId` = %s\n" +
                "WHERE `Id` = %s;\n", doctor.getName(), doctor.getRoom(), doctor.getPhone(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(doctor.getStartTime()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(doctor.getEndTime()), doctor.getSpecialityId(), doctor.getId());

        int result = driver.statement.executeUpdate(sql);
    }
}
