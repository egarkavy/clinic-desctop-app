package Model.Tables;

import java.util.Date;

/**
 * Created by super on 5/26/2019.
 */
public class Patient {
    private int Id;
    private String Name;
    private Date DOB;
    private String Phone;
    private String Address;
    private String PassportNumber;
    private int UserId;

    public Patient() {

    }

    public Patient(int userId) {
        UserId = userId;
    }

    public Patient(int id, String name, Date DOB, String phone, String address, String passportNumber, int userId) {
        Id = id;
        Name = name;
        this.DOB = DOB;
        Phone = phone;
        Address = address;
        PassportNumber = passportNumber;
        UserId = userId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
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

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPassportNumber() {
        return PassportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        PassportNumber = passportNumber;
    }
}
