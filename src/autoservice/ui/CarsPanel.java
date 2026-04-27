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

        return topPanel;
    }

    private JPanel getButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonPanel.setBackground(ColorsStorage.backgroundColor);

        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");

        addBtn.addActionListener(e -> openForm(null));
        editBtn.addActionListener(e -> {
            Car selected = getSelectedCar();
            if (selected != null) {
                openForm(selected);
            }
        });

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        return buttonPanel;
    }

    private JScrollPane getTable() {
        String[] columns = {"ID", "Марка", "Модель", "Гос. номер", "Год", "Владелец", "Телефон"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
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
            tableModel.addRow(new Object[]{c.getId(), c.getBrand(), c.getModel(), c.getLicensePlate(), c.getYear(), c.getOwnerName(), c.getOwnerPhone()});
        }
    }

    private void openForm(Car car) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        CarFormDialog dialog = new CarFormDialog(parent, car, carDAO);
        dialog.setVisible(true);
        loadTable(carDAO.findAll());
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
