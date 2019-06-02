package Model.Tables;

import java.util.Date;

/**
 * Created by super on 5/25/2019.
 */
public class Doctor {
    private int Id;
    private String Name;
    private int SpecialityId;
    private String SpecialityName;
    private String Login;
    private String Password;
    private int Room;
    private String Phone;
    private Date StartTime;
    private Date EndTime;
    private int UserId;

    public Doctor() {

    }

    public Doctor(String name, int specialityId, String login, String password, int room, String phone, Date startTime, Date endTime) {
        Name = name;
        SpecialityId = specialityId;
        Login = login;
        Password = password;
        Room = room;
        Phone = phone;
        StartTime = startTime;
        EndTime = endTime;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getSpecialityId() {
        return SpecialityId;
    }

    public void setSpecialityId(int specialityId) {
        SpecialityId = specialityId;
    }

    public String getSpecialityName() {
        return SpecialityName;
    }

    public void setSpecialityName(String specialityName) {
        SpecialityName = specialityName;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getRoom() {
        return Room;
    }

    public void setRoom(int room) {
        Room = room;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
