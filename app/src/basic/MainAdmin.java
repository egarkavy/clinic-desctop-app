package basic;

import Model.Repositories.NewsRepository;
import Model.Repositories.ToDoRepository;
import Model.Tables.ToDo;
import services.WindowService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by super on 5/12/2019.
 */
public class MainAdmin {

    public JPanel panel;
    private JTable table;
    private JButton specialitiesButton;
    private JButton doctorsButton;
    private JTextField whatField;
    private JCheckBox isDoneCheckBox;
    private JButton addButton;
    private JButton editButton;
    private JTextField newsField;
    private JButton addNewsButton;

    private List<ToDo> todos;
    private DefaultTableModel model;

    private ToDoRepository toDoRepository;
    private NewsRepository newsRepository;

    public MainAdmin() throws SQLException {
        toDoRepository = new ToDoRepository();
        newsRepository = new NewsRepository();

        Object[] columns = {"Id", "What", "Is Done" };
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        FillTable();

        specialitiesButton.addActionListener(e -> {
            try {
                WindowService.JustGoToVindow(new Specialities().panel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        doctorsButton.addActionListener(e -> {
            try {
                WindowService.JustGoToVindow(new Doctors().panel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        addButton.addActionListener(e -> {
            try {
                Add();
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
        editButton.addActionListener(e -> {
            try {
                Edit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        addNewsButton.addActionListener(e -> {
            try {
                newsRepository.Save(newsField.getText());
                JOptionPane.showMessageDialog(null, "News added", "InfoBox:", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "Something gone wrong", "InfoBox:", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void Add() throws SQLException {
        String what = whatField.getText();
        boolean isDone = isDoneCheckBox.isSelected();

        ToDo todo = new ToDo(what, isDone);

        toDoRepository.Save(todo);

        FillTable();
    }

    private void Edit() throws SQLException {
        ToDo todo = GetToDoFromTable();

        FillToDoFromEditFields(todo);

        toDoRepository.Update(todo);

        FillTable();
    }

    private void FillToDoFromEditFields(ToDo todo) {
        todo.setDone(isDoneCheckBox.isSelected());
        todo.setWhat(whatField.getText());
    }

    private ToDo GetToDoFromTable() {
        int i = table.getSelectedRow();

        int todoId = Integer.parseInt(model.getValueAt(i, 0).toString());

        ToDo toDo = todos.stream().filter(x -> x.getId() == todoId).findFirst().get();

        return toDo;
    }

    private void FillEditField() {
        ToDo todo = GetToDoFromTable();

        FillEditField(todo);
    }

    private void FillEditField(ToDo todo) {
        whatField.setText(todo.getWhat());
        isDoneCheckBox.setSelected(todo.isDone());
    }

    public void FillTable() throws SQLException {
        model.setRowCount(0);

        todos = toDoRepository.Get();
        FillTable(todos);
    }

    public void FillTable(List<ToDo> todos) {
        for(ToDo todo : todos) {
            Object[] row = new Object[3];

            row[0] = todo.getId();
            row[1] = todo.getWhat();
            row[2] = todo.isDone() ? "Yes" : "No";

            model.addRow(row);
        }
    }
}
