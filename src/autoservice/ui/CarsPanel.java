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
    private JTable table;
    private JComboBox<String> searchCombo;
    private JTextField searchField;

    public CarsPanel() {
        super();

        add(getTopPanel(), BorderLayout.NORTH);
        add(getTable(), BorderLayout.CENTER);

        loadTable(carDAO.findAll());
    }

    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColorsStorage.backgroundColor);
        topPanel.add(getButtonsPanel(), BorderLayout.NORTH);
        topPanel.add(getSearchPanel(), BorderLayout.SOUTH);

        return topPanel;
    }

    private JPanel getButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonPanel.setBackground(ColorsStorage.backgroundColor);

        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");

        addBtn.addActionListener(_ -> openForm(null));
        editBtn.addActionListener(_ -> {
            Car selected = getSelectedCar();
            if (selected != null) {
                openForm(selected);
            }
        });
        deleteBtn.addActionListener(_ -> deleteSelected());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        return buttonPanel;
    }

    private JPanel getSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        searchPanel.setBackground(ColorsStorage.backgroundColor);

        searchCombo = new JComboBox<>(new String[]{"Марке", "Владельцу", "Номеру"});
        searchField = new JTextField(18);
        JButton searchBtn = new JButton("Найти");
        JButton resetBtn = new JButton("Сбросить");

        searchBtn.addActionListener(_ -> doSearch());
        resetBtn.addActionListener(_ -> {
            searchField.setText("");
            loadTable(carDAO.findAll());
        });

        searchPanel.add(new JLabel("Поиск по:"));
        searchPanel.add(searchCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(resetBtn);

        return searchPanel;
    }

    private JScrollPane getTable() {
        String[] columns = {"ID", "Марка", "Модель", "Гос. номер", "Год", "Владелец", "Телефон"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(table);
    }

    private void loadTable(List<Car> cars) {
        tableModel.setRowCount(0);
        for (Car c : cars) {
            tableModel.addRow(new Object[]{c.getId(), c.getBrand(), c.getModel(), c.getLicensePlate(), c.getYear(), c.getOwnerName(), c.getOwnerPhone()});
        }
    }

    private void doSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadTable(carDAO.findAll());
            return;
        }

        int idx = searchCombo.getSelectedIndex();
        List<Car> result;
        if (idx == 0) {
            result = carDAO.searchByBrand(query);
        } else if (idx == 1) {
            result = carDAO.searchByOwner(query);
        } else {
            result = carDAO.searchByPlate(query);
        }

        loadTable(result);
    }

    private void openForm(Car car) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        CarFormDialog dialog = new CarFormDialog(parent, car, carDAO);
        dialog.setVisible(true);
        loadTable(carDAO.findAll());
    }

    private void deleteSelected() {
        Car car = getSelectedCar();
        if (car == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Удалить " + car.getBrand() + " " + car.getModel() + "?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            carDAO.delete(car.getId());
            loadTable(carDAO.findAll());
        }
    }

    private Car getSelectedCar() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Выберите строку в таблице.");
            return null;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        for (Car c : carDAO.findAll()) {
            if (c.getId() == id) {
                return c;
            }
        }

        return null;
    }
}
