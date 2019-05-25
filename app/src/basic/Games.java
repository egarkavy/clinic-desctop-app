package basic;

import Model.Repositories.PlayersRepository;
import services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by super on 5/19/2019.
 */
public class Games {
    public JPanel panel;
    private JTable table;
    private JButton addBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JTextField firstIdField;
    private JTextField secondIdField;
    private JTextField winnerField;
    private JTextField fScoreField;
    private JTextField sScoreField;
    private JLabel errorLabel;
    private JPanel editPanel;
    private JTextField dateFIeld;

    private List<Game> games;
    private PlayersRepository playersRepository;
    private CountryRepository countryRepository;
    private TeamRepository teamRepository;
    private GamesRepository gamesRepository;

    private DefaultTableModel model;

    public Games() throws SQLException {
        playersRepository = new PlayersRepository();
        countryRepository = new CountryRepository();
        teamRepository = new TeamRepository();
        gamesRepository = new GamesRepository();

        if (UserService.currentUser.getRoleId() == 2) {
            editPanel.setVisible(false);
        }

        Object[] columns = {"Id", "First Team", "Second Team", "Winner Name", "First Team Score", "Second Team Score", "Date Of Game"};
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
            }
        });
    }

    private void Add() throws SQLException, ParseException {
        String fId = firstIdField.getText();
        String sId = secondIdField.getText();
        String winnerId = winnerField.getText();
        String fScore = fScoreField.getText();
        String sScore = sScoreField.getText();
        String date = dateFIeld.getText();

        String error = ValidateGame(fId, sId, winnerId, fScore, sScore, date);

        if (error.length() > 0) {
            errorLabel.setText(error);
            return;
        }

        Game game = new Game(Integer.parseInt(fId), Integer.parseInt(sId), Integer.parseInt(winnerId), new SimpleDateFormat("dd/MM/yyyy").parse(date), Integer.parseInt(fScore), Integer.parseInt(sScore));

        gamesRepository.Save(game);

        FillTable();
    }

    private void Delete() throws SQLException {
        int i = table.getSelectedRow();

        if (i >= 0) {
            Game game = GetGameFromTable();

            gamesRepository.Delete(game.getId());
        }

        FillTable();
    }

    private String ValidateGame(String firstId, String secondId, String winner, String fScore, String sScore, String date) throws SQLException {
        String error = "";

        if (firstId == null || secondId == null || winner == null || fScore == null || sScore == null || date == null) {
            error = "All fields are required";

            return error;
        }

        if (!TryParseInt(firstId)) {
            error = "First team Id must be int";
            return error;
        }

        if (!TryParseInt(secondId)) {
            error = "Second team Id must be int";
            return error;
        }

        if (!TryParseInt(winner)) {
            error = "winner must be int";
            return error;
        }

        if (TryParseInt(winner)) {
            int winnerId = Integer.parseInt(winner);

            if (winnerId != 1 && winnerId != 2) {
                error = "winner must be in range of 1 or 2";
            }
        }

        if (!TryParseInt(fScore)) {
            error = "first Score must be int";
            return error;
        }

        if (!TryParseInt(sScore)) {
            error = "second Score be int";
            return error;
        }

        if (!TryParseDate(date)) {
            error = "date must be date";

            return error;
        }

        List<Team> teams = teamRepository.Get();

        int fId = Integer.parseInt(firstId);
        int sId = Integer.parseInt(secondId);

        if (!teams.stream().anyMatch(x -> x.getId() == fId)) {
            error = "Choose correct first team Id";
            return error;
        }
        if (!teams.stream().anyMatch(x -> x.getId() == sId)) {
            error = "Choose correct second team Id";
            return error;
        }

        return error;
    }

    private void Edit() throws SQLException {
        Game game = GetGameFromTable();

        FillGameFromEditFields(game);

        gamesRepository.Update(game);

        FillTable();
    }

    private void FillGameFromEditFields(Game game) {
        game.setFirstTeamId(Integer.parseInt(firstIdField.getText()));
        game.setSecondTeamId(Integer.parseInt(secondIdField.getText()));
        game.setWinnerId(Integer.parseInt(winnerField.getText()));
        game.setFirstTeamScore(Integer.parseInt(fScoreField.getText()));
        game.setSecondTeamScore(Integer.parseInt(sScoreField.getText()));
        try {
            game.setDateOfGame(new SimpleDateFormat("dd/MM/yyyy").parse(dateFIeld.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Game GetGameFromTable() {
        int i = table.getSelectedRow();

        int gameId = Integer.parseInt(model.getValueAt(i, 0).toString());

        Game game = games.stream().filter(x -> x.getId() == gameId).findFirst().get();

        return game;
    }

    private void FillEditField() {
        Game game = GetGameFromTable();

        FillEditField(game);
    }

    private void FillEditField(Game game) {
        firstIdField.setText(Integer.toString(game.getFirstTeamId()));
        secondIdField.setText(Integer.toString(game.getSecondTeamId()));
        sScoreField.setText(Integer.toString(game.getSecondTeamScore()));
        fScoreField.setText(Integer.toString(game.getFirstTeamScore()));
        winnerField.setText(Integer.toString(game.getWinnerId()));
        dateFIeld.setText(game.getDateOfGame().toString());
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
            new SimpleDateFormat("dd/MM/yyyy").parse(val);
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

        games = gamesRepository.Get();
        FillTable(games);
    }

    public void FillTable(List<Game> games) {
        for(Game game : games) {
            Object[] row = new Object[7];

            row[0] = game.getId();
            row[1] = game.getFirstTeamName();
            row[2] = game.getSecondTeamName();
            row[3] = game.getWinnerName();
            row[4] = game.getFirstTeamScore();
            row[5] = game.getSecondTeamScore();
            row[6] = game.getDateOfGame();

            model.addRow(row);
        }
    }
}
