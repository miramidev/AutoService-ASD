package autoservice.ui.panels;

import autoservice.dao.CarDAO;
import autoservice.helper.ColorsStorage;
import autoservice.model.Car;
import autoservice.ui.CarFormDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CarsPanel extends BasePanel {

    private final CarDAO carDAO = new CarDAO();

    public CarsPanel() {
        super();
        loadTable();
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"ID", "Марка", "Модель", "Гос. номер", "Год", "Владелец", "Телефон"};
    }

    @Override
    protected JPanel getButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(ColorsStorage.backgroundColor);

        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");

        addBtn.addActionListener(_ -> openForm(null));
        editBtn.addActionListener(_ -> {
            Car selected = getSelectedCar();
            if (selected != null) openForm(selected);
        });
        deleteBtn.addActionListener(_ -> deleteSelected());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        return panel;
    }

    @Override
    protected JPanel getSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(ColorsStorage.backgroundColor);

        searchCombo = new JComboBox<>(new String[]{"Марке", "Владельцу", "Номеру"});
        searchField = new JTextField(18);
        JButton searchBtn = new JButton("Найти");
        JButton resetBtn = new JButton("Сбросить");

        searchBtn.addActionListener(_ -> doSearch());
        resetBtn.addActionListener(_ -> {
            searchField.setText("");
            loadTable();
        });

        panel.add(new JLabel("Поиск по:"));
        panel.add(searchCombo);
        panel.add(searchField);
        panel.add(searchBtn);
        panel.add(resetBtn);
        return panel;
    }

    @Override
    protected void doSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadTable();
            return;
        }

        int idx = searchCombo.getSelectedIndex();
        List<Car> result;
        if (idx == 0) result = carDAO.searchByBrand(query);
        else if (idx == 1) result = carDAO.searchByOwner(query);
        else result = carDAO.searchByPlate(query);

        fillTable(result);
    }

    @Override
    protected void loadTable() {
        fillTable(carDAO.findAll());
    }

    private void fillTable(List<Car> cars) {
        tableModel.setRowCount(0);
        for (Car c : cars) {
            tableModel.addRow(new Object[]{
                    c.getId(), c.getBrand(), c.getModel(),
                    c.getLicensePlate(), c.getYear(),
                    c.getOwnerName(), c.getOwnerPhone()
            });
        }
    }

    private void openForm(Car car) {
        CarFormDialog dialog = new CarFormDialog(getParentFrame(), car, carDAO);
        dialog.setVisible(true);
        loadTable();
    }

    private void deleteSelected() {
        int id = getSelectedId();
        if (id == -1) return;
        Car car = getSelectedCar();
        if (car != null && showDeleteConfirm("Удалить " + car.getBrand() + " " + car.getModel() + "?") == JOptionPane.YES_OPTION) {
            carDAO.delete(id);
            loadTable();
        }
    }

    private Car getSelectedCar() {
        int id = getSelectedId();
        if (id == -1) return null;
        for (Car c : carDAO.findAll())
            if (c.getId() == id) return c;
        return null;
    }
}