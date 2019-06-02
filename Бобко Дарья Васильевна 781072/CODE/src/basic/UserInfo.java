package basic;

import Model.Repositories.DoctorsRepository;
import Model.Repositories.PatientRepository;
import Model.Tables.Doctor;
import Model.Tables.Patient;
import services.UserService;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by super on 5/26/2019.
 */
public class UserInfo {
    public JPanel panel;
    private JPanel patientPanel;
    private JPanel doctorPanel;
    private JButton saveBtn;
    private JTextField patientNameField;
    private JTextField patientDobField;
    private JTextField patientPhoneField;
    private JTextField patientAddressField;
    private JTextField patientPassportNumField;
    private JLabel docNameField;
    private JLabel docRoomField;
    private JLabel docStartField;
    private JLabel docEndField;
    private JLabel docPhoneField;
    private JLabel docSpecialityField;

    private DoctorsRepository doctorsRepository;
    private PatientRepository patientRepository;

    private Patient patient;

    public UserInfo() throws SQLException {
        doctorsRepository = new DoctorsRepository();
        patientRepository = new PatientRepository();

        FillFields();
        saveBtn.addActionListener(e -> {

            try {
                patientRepository.Update(new Patient(patient.getId(), patientNameField.getText(), new SimpleDateFormat("dd/MM/yyyy").parse(patientDobField.getText()), patientPhoneField.getText(), patientAddressField.getText(), patientPassportNumField.getText(), patient.getUserId()));
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void FillFields() throws SQLException {
        if (UserService.currentUser.getRoleId() == 3) { //DOCTOR
            patientPanel.setVisible(false);
            FillDocFields();

        }
        if (UserService.currentUser.getRoleId() == 2) { //PATIENT
            doctorPanel.setVisible(false);
            FillPatientFields();
        }
    }

    private void FillDocFields() throws SQLException {
        Doctor doc = doctorsRepository.Get().stream().filter(x -> x.getUserId() == UserService.currentUser.getId()).findFirst().get();

        docNameField.setText(doc.getName());
        docRoomField.setText(Integer.toString(doc.getRoom()));
        docStartField.setText(doc.getStartTime().toString());
        docEndField.setText(doc.getEndTime().toString());
        docPhoneField.setText(doc.getPhone());
        docSpecialityField.setText(doc.getSpecialityName());
    }

    private void FillPatientFields() throws SQLException {
        patient = patientRepository.Get().stream().filter(x -> x.getUserId() == UserService.currentUser.getId()).findFirst().get();

        patientNameField.setText(patient.getName());
        if (patient.getDOB() != null) {
            patientDobField.setText(patient.getDOB().toString());
        }

        if (patient.getAddress() != null) {
            patientAddressField.setText(patient.getAddress());
        }

        if (patient.getPhone() != null) {
            patientPhoneField.setText(patient.getPhone());
        }

        if (patient.getPassportNumber() != null) {
            patientPassportNumField.setText(patient.getPassportNumber());
        }
    }
}
