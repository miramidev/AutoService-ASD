package autoservice.ui;

import autoservice.dao.CarDAO;
import autoservice.helper.ColorsStorage;
import autoservice.model.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CarsPanel extends BasePanel {
    private final CarDAO carDAO = new CarDAO();
    private DefaultTableModel tableModel;

    public CarsPanel() {
        super();

        add(getTopPanel(), BorderLayout.NORTH);
        add(getTable(), BorderLayout.CENTER);

        loadTable(carDAO.findAll());
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
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(table);
    }

    private void loadTable(List<Car> cars) {
        tableModel.setRowCount(0);
        for (Car c : cars) {
            tableModel.addRow(new Object[]{
                    c.getId(), c.getBrand(), c.getModel(),
                    c.getLicensePlate(), c.getYear(),
                    c.getOwnerName(), c.getOwnerPhone()
            });
        }
    }
}
