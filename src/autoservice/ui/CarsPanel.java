package autoservice.ui;

import autoservice.helper.ColorsStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarsPanel extends BasePanel {
    public CarsPanel() {
        super();

        var topPanel = getTopPanel();
        var table = getTable();

        add(table, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
    }

    private JPanel getTopPanel() {
        var buttonPanel = getButtonsPanel();
        var searchPanel = getSearchPanel();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColorsStorage.backgroundColor);
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        return topPanel;
    }

    private JPanel getButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonPanel.setBackground(ColorsStorage.backgroundColor);

        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        return buttonPanel;
    }

    private JPanel getSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        searchPanel.setBackground(ColorsStorage.backgroundColor);

        JLabel searchLabel = new JLabel("Поиск по:");
        JComboBox<String> searchCombo = new JComboBox<>(new String[]{"Марке", "Владельцу", "Номеру"});
        JTextField searchField = new JTextField(18);
        JButton searchBtn = new JButton("Найти");
        JButton resetBtn = new JButton("Сбросить");

        searchPanel.add(searchLabel);
        searchPanel.add(searchCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(resetBtn);

        return searchPanel;
    }

    private JScrollPane getTable() {
        String[] columns = {"ID", "Марка", "Модель", "Гос. номер", "Год", "Владелец", "Телефон"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        return new JScrollPane(table);
    }
}
