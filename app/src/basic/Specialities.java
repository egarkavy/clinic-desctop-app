package basic;

import Model.Repositories.SpecialityRepository;
import Model.Tables.Speciality;
import services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by super on 5/19/2019.
 */
public class Specialities {
    public JPanel panel;
    private JTable table;
    private JButton addBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JTextField nameField;
    private JLabel errorLabel;
    private JPanel editPanel;

    private List<Speciality> specialities;
    private SpecialityRepository specialityRepository;

    private DefaultTableModel model;

    public Specialities() throws SQLException {
        specialityRepository = new SpecialityRepository();

        if (UserService.currentUser.getRoleId() == 2) {
            editPanel.setVisible(false);
        }

        Object[] columns = {"Id", "Name" };
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        FillTable();

        addBtn.addActionListener(e -> {
            try {
                Add();
            } catch (SQLException e1) {
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

    private void Add() throws SQLException {
        String name = nameField.getText();

        String error = ValidateSpeciality(name);

        if (error.length() > 0) {
            errorLabel.setText(error);
            return;
        }

        Speciality speciality = new Speciality(name);

        specialityRepository.Save(speciality);

        FillTable();
    }

    private void Delete() throws SQLException {
        int i = table.getSelectedRow();

        if (i >= 0) {
            Speciality speciality = GetTeamFromTable();

            specialityRepository.Delete(speciality.getId());
        }

        FillTable();
    }

    private String ValidateSpeciality(String name) throws SQLException {
        String error = "";
        if (name == null) {
            error = "All fields are required";

            return error;
        }

        return error;
    }

    private void Edit() throws SQLException {
        Speciality speciality = GetTeamFromTable();

        FillSpecialityFromEditFields(speciality);

        specialityRepository.Update(speciality);

        FillTable();
    }

    private void FillSpecialityFromEditFields(Speciality speciality) {
        speciality.setName(nameField.getText());
    }

    private Speciality GetTeamFromTable() {
        int i = table.getSelectedRow();

        int teamId = Integer.parseInt(model.getValueAt(i, 0).toString());

        Speciality speciality = specialities.stream().filter(x -> x.getId() == teamId).findFirst().get();

        return speciality;
    }

    private void FillEditField() {
        Speciality speciality= GetTeamFromTable();

        FillEditField(speciality);
    }

    private void FillEditField(Speciality speciality) {
        nameField.setText(speciality.getName());
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

        specialities = specialityRepository.Get();
        FillTable(specialities);
    }

    public void FillTable(List<Speciality> specialities) {
        for(Speciality speciality : specialities) {
            Object[] row = new Object[2];

            row[0] = speciality.getId();
            row[1] = speciality.getName();

            model.addRow(row);
        }
    }
}
