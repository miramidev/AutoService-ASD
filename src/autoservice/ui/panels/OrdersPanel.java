package autoservice.ui.panels;

import autoservice.dao.CarDAO;
import autoservice.dao.OrderDAO;
import autoservice.helper.ColorsStorage;
import autoservice.model.Order;
import autoservice.model.Order.Status;
import autoservice.ui.OrderFormDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrdersPanel extends BasePanel {
    private final OrderDAO orderDAO = new OrderDAO();
    private final CarDAO carDAO = new CarDAO();
    private JComboBox<Status> statusCombo;

    public OrdersPanel() {
        super();
        loadTable();
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"ID", "Автомобиль", "Описание", "Мастер", "Стоимость", "Дата приёма", "Дата готовности", "Статус"};
    }

    @Override
    protected JPanel getButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(ColorsStorage.backgroundColor);

        JButton addBtn = new JButton("Новый заказ");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");

        addBtn.addActionListener(_ -> openForm(null));
        editBtn.addActionListener(_ -> {
            Order selected = getSelectedOrder();
            if (selected != null) {
                openForm(selected);
            }
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

        searchCombo = new JComboBox<>(new String[]{"Мастеру", "Дате", "Статусу"});
        searchField = new JTextField(16);
        statusCombo = new JComboBox<>(Status.values());
        statusCombo.setVisible(false);

        searchCombo.addActionListener(_ -> {
            boolean isStatus = searchCombo.getSelectedIndex() == 2;
            searchField.setVisible(!isStatus);
            statusCombo.setVisible(isStatus);
        });

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
        panel.add(statusCombo);
        panel.add(searchBtn);
        panel.add(resetBtn);

        return panel;
    }

    @Override
    protected void doSearch() {
        int idx = searchCombo.getSelectedIndex();
        List<Order> result;

        if (idx == 0) {
            result = orderDAO.searchByMaster(searchField.getText().trim());
        } else if (idx == 1) {
            result = orderDAO.searchByDate(searchField.getText().trim());
        } else {
            result = orderDAO.searchByStatus((Status) statusCombo.getSelectedItem());
        }

        fillTable(result);
    }

    @Override
    protected void loadTable() {
        fillTable(orderDAO.findAll());
    }

    private void fillTable(List<Order> orders) {
        tableModel.setRowCount(0);
        for (Order o : orders) {
            tableModel.addRow(new Object[]{o.getId(), o.getCarInfo(), o.getDescription(), o.getMasterName(), o.getCost(),
                    o.getDateCreated(), o.getDateCompleted(), o.getStatus().name()});
        }
    }

    private void openForm(Order order) {
        OrderFormDialog dialog = new OrderFormDialog(getParentFrame(), order, orderDAO, carDAO);
        dialog.setVisible(true);
        loadTable();
    }

    private void deleteSelected() {
        int id = getSelectedId();
        if (id == -1) {
            return;
        }

        if (showDeleteConfirm("Удалить заказ #" + id + "?") == JOptionPane.YES_OPTION) {
            orderDAO.delete(id);
            loadTable();
        }
    }

    private Order getSelectedOrder() {
        int id = getSelectedId();
        if (id == -1) {
            return null;
        }

        for (Order o : orderDAO.findAll()) {
            if (o.getId() == id) {
                return o;
            }
        }

        return null;
    }
}