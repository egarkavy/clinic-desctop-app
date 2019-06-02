package basic;

import Model.Repositories.DoctorsRepository;
import Model.Repositories.SpecialityRepository;
import Model.Tables.Doctor;
import Model.Tables.Speciality;
import services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Doctors {
    public JPanel panel;
    private JTable table;
    private JButton addBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JTextField nameField;
    private JTextField loginField;
    private JTextField passField;
    private JTextField roomField;
    private JTextField phoneField;
    private JLabel errorLabel;
    private JPanel editPanel;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextField specialityIdField;

    private List<Doctor> doctors;
    private DoctorsRepository doctorsRepository;
    private SpecialityRepository specialityRepository;

    private DefaultTableModel model;

    public Doctors() throws SQLException {
        doctorsRepository = new DoctorsRepository();
        specialityRepository = new SpecialityRepository();

        if (UserService.currentUser.getRoleId() == 2) {
            editPanel.setVisible(false);
        }

        Object[] columns = {"Id", "Full Name", "Room", "Phone", "Start work time", "End work time", "Speciality"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        FillTable();
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
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void Add() throws SQLException, ParseException {
        String name = nameField.getText();
        String login = loginField.getText();
        String pass = passField.getText();
        String phone = phoneField.getText();
        String room = roomField.getText();
        String startT = startTimeField.getText();
        String endT = endTimeField.getText();
        String sId = specialityIdField.getText();

        String error = ValidateDoctor(name, login, pass, phone, room, startT, endT, sId);

        if (error.length() > 0) {
            errorLabel.setText(error);
            return;
        }

        Doctor doctor = new Doctor(name, Integer.parseInt(sId), login, pass, Integer.parseInt(room), phone, new SimpleDateFormat("hh:mm").parse(startT), new SimpleDateFormat("hh:mm").parse(endT));

        doctorsRepository.Save(doctor);

        FillTable();
    }

    private void Delete() throws SQLException {
        int i = table.getSelectedRow();

        if (i >= 0) {
            Doctor doctor = GetDoctorFromTable();

            doctorsRepository.Delete(doctor.getId());
        }

        FillTable();
    }

    private String ValidateDoctor(String name, String login, String password, String phone, String room, String startT, String endT, String specialityId) throws SQLException {
        String error = "";
        if (name == null || login == null || password == null || phone == null || room == null || startT == null || endT == null || specialityId == null) {
            error = "All fields are required";

            return error;
        }

        if (!TryParseInt(room)) {
            error = "Room must be int";
            return error;
        }

        if (!TryParseInt(specialityId)) {
            error = "Speciality must be int";
            return error;
        }

        List<Speciality> specialities = specialityRepository.Get();

        int sId = Integer.parseInt(specialityId);

        if (!specialities.stream().anyMatch(x -> x.getId() == sId)) {
            error = "Choose correct speciality Id";
            return error;
        }

        if (!TryParseDate(startT)) {
            error = "start time must time hh:mm";

            return error;
        }

        if (!TryParseDate(endT)) {
            error = "end time must be time hh:mm";

            return error;
        }

        return error;
    }

    private boolean TryParseDate(String val) {
        try {
            new SimpleDateFormat("hh:mm").parse(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void Edit() throws SQLException, ParseException {
        Doctor doctor = GetDoctorFromTable();

        FillDoctorFromEditFields(doctor);

        String error = ValidateDoctor(doctor.getName(), doctor.getLogin(), doctor.getPassword(), doctor.getPhone(), Integer.toString(doctor.getRoom()), new SimpleDateFormat("hh:mm").format(doctor.getStartTime()), new SimpleDateFormat("hh:mm").format(doctor.getEndTime()), Integer.toString(doctor.getSpecialityId()));

        if (error.length() > 0) {
            errorLabel.setText(error);
            return;
        }

        doctorsRepository.Update(doctor);

        FillTable();
    }

    private void FillDoctorFromEditFields(Doctor doctor) throws ParseException {
        doctor.setName(nameField.getText());
        doctor.setPhone(phoneField.getText());
        doctor.setRoom(Integer.parseInt(roomField.getText()));
        doctor.setStartTime(new SimpleDateFormat("hh:mm").parse(startTimeField.getText()));
        doctor.setEndTime(new SimpleDateFormat("hh:mm").parse(endTimeField.getText()));
        doctor.setSpecialityId(Integer.parseInt(specialityIdField.getText()));
        doctor.setLogin(loginField.getText());
        doctor.setPassword(passField.getText());
    }

    private Doctor GetDoctorFromTable() {
        int i = table.getSelectedRow();

        int doctorId = Integer.parseInt(model.getValueAt(i, 0).toString());

        Doctor player = doctors.stream().filter(x -> x.getId() == doctorId).findFirst().get();

        return player;
    }

    private void FillEditField() {
        Doctor doctor = GetDoctorFromTable();

        FillEditField(doctor);
    }

    private void FillEditField(Doctor doctor) {
        nameField.setText(doctor.getName());
        loginField.setText(doctor.getLogin());
        passField.setText(doctor.getPassword());
        roomField.setText(Integer.toString(doctor.getRoom()));
        phoneField.setText(doctor.getPhone());
        startTimeField.setText(doctor.getStartTime().toString());
        endTimeField.setText(doctor.getEndTime().toString());
        specialityIdField.setText(Integer.toString(doctor.getSpecialityId()));
    }

    private boolean TryParseInt(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void FillTable() throws SQLException {
        model.setRowCount(0);

        doctors = doctorsRepository.Get();
        FillTable(doctors);
    }

    public void FillTable(List<Doctor> doctors) {
        for(Doctor player : doctors) {
            Object[] row = new Object[7];

            row[0] = player.getId();
            row[1] = player.getName();
            row[2] = player.getRoom();
            row[3] = player.getPhone();
            row[4] = player.getStartTime();
            row[5] = player.getEndTime();
            row[6] = player.getSpecialityName();

            model.addRow(row);
        }
    }


}
