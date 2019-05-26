package basic;

import Model.Repositories.DoctorsRepository;
import Model.Repositories.PatientRepository;
import Model.Repositories.ReceptionRepository;
import Model.Tables.Doctor;
import Model.Tables.Patient;
import Model.Tables.Reception;
import services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by super on 5/19/2019.
 */
public class Receptions {
    public JPanel panel;
    private JTable table;
    private JButton addBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JTextField dateField;
    private JTextField doctorIdField;
    private JTextField patientIdField;
    private JTextField detailsField;
    private JLabel errorLabel;
    private JTable doctorsTable;
    private JTable patientsTable;
    private JScrollPane doctorsPanel;
    private JScrollPane patientPanel;

    private List<Reception> receptions;
    private List<Doctor> doctors;
    private List<Patient> patients;

    private ReceptionRepository receptionRepository;
    private DoctorsRepository doctorsRepository;
    private PatientRepository patientRepository;

    private DefaultTableModel model;
    private DefaultTableModel doctorModel;
    private DefaultTableModel patientModel;

    public Receptions() throws SQLException {
        receptionRepository = new ReceptionRepository();
        doctorsRepository = new DoctorsRepository();
        patientRepository = new PatientRepository();

        if (UserService.currentUser.getRoleId() == 3) { //DOCTOR
            doctorsPanel.setVisible(false);
        }

        if (UserService.currentUser.getRoleId() == 2) { //Patient
            patientPanel.setVisible(false);
        }

        Object[] columns = {"Id", "Date", "Doctor", "Patient", "Details", "Room"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        Object[] doctorColumns = {"Id", "Name", "Speciality", "Start Time", "End Time", "Room", "Phone"};
        doctorModel = new DefaultTableModel();
        doctorModel.setColumnIdentifiers(doctorColumns);
        doctorsTable.setModel(doctorModel);

        Object[] patientColumns = {"Id", "Name", "DOB", "Passport Number", "Phone"};
        patientModel = new DefaultTableModel();
        patientModel.setColumnIdentifiers(patientColumns);
        patientsTable.setModel(patientModel);

        FillTable();
        FillDoctorAndPatientTables();

        addBtn.addActionListener(e -> {
            try {
                Add();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });
        deleteBtn.addActionListener(e -> {
            try {
                Delete();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                FillEditField();
            }
        });
        editBtn.addActionListener(e -> {
            try {
                Edit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void Add() throws SQLException, ParseException {
        String date = dateField.getText();
        String doctorId = doctorIdField.getText();
        String patientId = patientIdField.getText();
        String details = detailsField.getText();

        String error = ValidateReception(date, doctorId, patientId);

        if (error.length() > 0) {
            errorLabel.setText(error);
            return;
        }

        Reception reception = new Reception(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(date), Integer.parseInt(doctorId), Integer.parseInt(patientId), details);

        receptionRepository.Save(reception);

        FillTable();
    }

    private void Delete() throws SQLException {
        int i = table.getSelectedRow();

        if (i >= 0) {
            Reception reception = GetReceptionFromTable();

            receptionRepository.Delete(reception.getId());
        }

        FillTable();
    }

    private String ValidateReception(String date, String doctorId, String PatientId) throws SQLException, ParseException {
        String error = "";

        if (date == null || doctorId == null || PatientId == null) {
            error = "date, doctor, patient fields are required";

            return error;
        }

        if (!TryParseInt(doctorId)) {
            error = "Doctor ID must be int";
            return error;
        }

        if (!TryParseInt(PatientId)) {
            error = "Patient Id must be int";
            return error;
        }

        if (!TryParseDate(date)) {
            error = "date must be date";

            return error;
        }

        int docId = Integer.parseInt(doctorId);

        if (!doctors.stream().anyMatch(x -> x.getId() == docId)) {
            error = "Chose existing doctorId (use table)";
            return error;
        }

        Doctor chosenDoc = doctorsRepository.Get().stream().filter(x -> x.getId() == docId).findFirst().get();
        List<Reception> allReceptions = receptionRepository.Get().stream().filter(x -> x.getDoctorId() == chosenDoc.getId()).collect(Collectors.toList());

        Date selectedDate = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(date);;
        if (!(
                (chosenDoc.getStartTime().getMinutes() + chosenDoc.getStartTime().getHours() * 60) <= (selectedDate.getMinutes() + selectedDate.getHours() * 60) &&
                (chosenDoc.getEndTime().getMinutes() + chosenDoc.getEndTime().getHours() * 60) >=     (selectedDate.getMinutes() + selectedDate.getHours() * 60))) {
            error = "Selected date is not in bounds of working day of a doctor";
            return error;
        }

        Date endOfReception = AddMinutes(selectedDate, 10);

        if(allReceptions.stream().anyMatch(x ->  {
            Date startRecTime = x.getDateTime();
            Date endTime = AddMinutes(startRecTime, 10);
            return ((selectedDate.compareTo(startRecTime) >= 0 && selectedDate.compareTo(endTime) <= 0) || (endOfReception.compareTo(startRecTime) >= 0 && endOfReception.compareTo(endTime) <= 0));
        })) {
            error = "Selected date is in bounds of another reception time of a doctor, choose another time";
            return error;
        }

        return error;
    }

    private Date AddMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes); //minus number would decrement the days
        Date result = cal.getTime();
        return result;
    }

    private void Edit() throws SQLException {
        Reception reception = GetReceptionFromTable();

        FillReceptionFromEditFields(reception);

        receptionRepository.Update(reception);

        FillTable();
    }

    private void FillReceptionFromEditFields(Reception reception) {
        reception.setDoctorId(Integer.parseInt(doctorIdField.getText()));
        reception.setPatientId(Integer.parseInt(patientIdField.getText()));
        reception.setDetails(detailsField.getText());
        try {
            reception.setDateTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(dateField.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Reception GetReceptionFromTable() {
        int i = table.getSelectedRow();

        int receptionId = Integer.parseInt(model.getValueAt(i, 0).toString());

        Reception reception = receptions.stream().filter(x -> x.getId() == receptionId).findFirst().get();

        return reception;
    }

    private void FillEditField() {
        Reception reception = GetReceptionFromTable();

        FillEditField(reception);
    }

    private void FillEditField(Reception reception) {
        dateField.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(reception.getDateTime()));
        doctorIdField.setText(Integer.toString(reception.getDoctorId()));
        patientIdField.setText(Integer.toString(reception.getPatientId()));
        detailsField.setText(reception.getDetails());
    }

    private boolean TryParseInt(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean TryParseDate(String val) {
        try {
            new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void FillTable() throws SQLException {
        model.setRowCount(0);

        receptions = receptionRepository.Get();
        FillTable(receptions);
    }

    public void FillDoctorAndPatientTables() throws SQLException {
        doctorModel.setRowCount(0);
        patientModel.setRowCount(0);

        doctors = doctorsRepository.Get();
        patients = patientRepository.Get();

        FillDoctorTable(doctors);
        FillPatientTable(patients);
    }

    public void FillDoctorTable(List<Doctor> doctors) {
        for(Doctor doctor : doctors) {
            Object[] row = new Object[7];

            row[0] = doctor.getId();
            row[1] = doctor.getName();
            row[2] = doctor.getSpecialityName();
            row[3] = doctor.getStartTime();
            row[4] = doctor.getEndTime();
            row[5] = doctor.getRoom();
            row[6] = doctor.getPhone();

            doctorModel.addRow(row);
        }
    }

    public void FillPatientTable(List<Patient> patients) {
        for(Patient patient : patients) {
            Object[] row = new Object[5];

            row[0] = patient.getId();
            row[1] = patient.getName();
            row[2] = patient.getDOB();
            row[3] = patient.getPassportNumber();
            row[4] = patient.getPhone();

            patientModel.addRow(row);
        }
    }

    public void FillTable(List<Reception> receptions) throws SQLException {
        if (UserService.currentUser.getRoleId() == 3) { //DOCTOR
            Doctor doctor = doctorsRepository.Get().stream().filter(x -> x.getUserId() == UserService.currentUser.getId()).findFirst().get();

            receptions = receptions.stream().filter(x -> x.getDoctorId() == doctor.getId()).collect(Collectors.toList());

            doctorIdField.setText(Integer.toString(doctor.getId()));
            doctorIdField.setEditable(false);
        }

        if (UserService.currentUser.getRoleId() == 2) { //Patient
            Patient patient = patientRepository.Get().stream().filter(x -> x.getUserId() == UserService.currentUser.getId()).findFirst().get();

            receptions = receptions.stream().filter(x -> x.getPatientId() == patient.getId()).collect(Collectors.toList());

            patientIdField.setText(Integer.toString(patient.getId()));
            patientIdField.setEditable(false);
        }

        for(Reception reception : receptions) {
            Object[] row = new Object[6];

            row[0] = reception.getId();
            row[1] = reception.getDateTime();
            row[2] = reception.getDoctorName();
            row[3] = reception.getPatientName();
            row[4] = reception.getDetails();
            row[5] = reception.getRoom();

            model.addRow(row);
        }
    }
}
