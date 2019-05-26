package Model.Tables;

import java.util.Date;

/**
 * Created by super on 5/26/2019.
 */
public class Reception {
    private int Id;
    private Date DateTime;
    private int DoctorId;
    private int PatientId;
    private String Details;
    private String PatientName;
    private String DoctorName;
    private int Room;
    private Date PatientDOB;

    public Reception() {

    }

    public Reception(Date dateTime, int doctorId, int patientId, String details) {
        DateTime = dateTime;
        DoctorId = doctorId;
        PatientId = patientId;
        Details = details;
    }

    public Reception(int id, Date dateTime, int doctorId, int patientId, String details, String patientName, String doctorName, int room, Date patientDOB) {
        Id = id;
        DateTime = dateTime;
        DoctorId = doctorId;
        PatientId = patientId;
        Details = details;
        PatientName = patientName;
        DoctorName = doctorName;
        Room = room;
        PatientDOB = patientDOB;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Date getDateTime() {
        return DateTime;
    }

    public void setDateTime(Date dateTime) {
        DateTime = dateTime;
    }

    public int getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(int doctorId) {
        DoctorId = doctorId;
    }

    public int getPatientId() {
        return PatientId;
    }

    public void setPatientId(int patientId) {
        PatientId = patientId;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public int getRoom() {
        return Room;
    }

    public void setRoom(int room) {
        Room = room;
    }

    public Date getPatientDOB() {
        return PatientDOB;
    }

    public void setPatientDOB(Date patientDOB) {
        PatientDOB = patientDOB;
    }
}
