package basic;

import Model.Repositories.PlayersRepository;
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
public class Teams {
    public JPanel panel;
    private JTable table;
    private JButton addBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JTextField nameField;
    private JTextField countryField;
    private JLabel errorLabel;
    private JPanel editPanel;

    private List<Team> teams;
    private PlayersRepository playersRepository;
    private CountryRepository countryRepository;
    private TeamRepository teamRepository;

    private DefaultTableModel model;

    public Teams() throws SQLException {
        playersRepository = new PlayersRepository();
        countryRepository = new CountryRepository();
        teamRepository = new TeamRepository();

        if (UserService.currentUser.getRoleId() == 2) {
            editPanel.setVisible(false);
        }

        Object[] columns = {"Id", "Name", "Country" };
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
        String country = countryField.getText();

        String error = ValidateTeam(name, country);

        if (error.length() > 0) {
            errorLabel.setText(error);
            return;
        }

        Team team = new Team(name, Integer.parseInt(country));

        teamRepository.Save(team);

        FillTable();
    }

    private void Delete() throws SQLException {
        int i = table.getSelectedRow();

        if (i >= 0) {
            Team team = GetTeamFromTable();

            teamRepository.Delete(team.getId());
        }

        FillTable();
    }

    private String ValidateTeam(String name, String country) throws SQLException {
        String error = "";
        if (name == null || country == null) {
            error = "All fields are required";

            return error;
        }

        if (!TryParseInt(country)) {
            error = "CountryId must be int";
            return error;
        }

        List<Country> countries = countryRepository.Get();

        int cId = Integer.parseInt(country);

        if (!countries.stream().anyMatch(x -> x.getId() == cId)) {
            error = "Choose correct county Id";
            return error;
        }

        return error;
    }

    private void Edit() throws SQLException {
        Team team = GetTeamFromTable();

        FillTeamFromEditFields(team);

        teamRepository.Update(team);

        FillTable();
    }

    private void FillTeamFromEditFields(Team team) {
        team.setCountryId(Integer.parseInt(countryField.getText()));
        team.setTeamName(nameField.getText());
    }

    private Team GetTeamFromTable() {
        int i = table.getSelectedRow();

        int teamId = Integer.parseInt(model.getValueAt(i, 0).toString());

        Team team = teams.stream().filter(x -> x.getId() == teamId).findFirst().get();

        return team;
    }

    private void FillEditField() {
        Team team = GetTeamFromTable();

        FillEditField(team);
    }

    private void FillEditField(Team team) {
        nameField.setText(team.getTeamName());
        countryField.setText(Integer.toString(team.getCountryId()));
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

        teams = teamRepository.Get();
        FillTable(teams);
    }

    public void FillTable(List<Team> teams) {
        for(Team team : teams) {
            Object[] row = new Object[3];

            row[0] = team.getId();
            row[1] = team.getTeamName();
            row[2] = team.getCountryName();

            model.addRow(row);
        }
    }
}
