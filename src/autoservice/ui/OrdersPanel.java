package autoservice.ui;

import autoservice.dao.CarDAO;
import autoservice.dao.OrderDAO;
import autoservice.helper.ColorsStorage;
import autoservice.model.Order;
import autoservice.model.Order.Status;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdersPanel extends BasePanel {
    private final OrderDAO orderDAO = new OrderDAO();
    private final CarDAO carDAO = new CarDAO();
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> searchCombo;
    private JTextField searchField;
    private JComboBox<Status> statusCombo;

    public OrdersPanel() {
        super();

        add(getTopPanel(), BorderLayout.NORTH);
        add(getTable(), BorderLayout.CENTER);

        loadTable(orderDAO.findAll());
    }

    private JPanel getTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(ColorsStorage.backgroundColor);
        top.add(getButtonsPanel(), BorderLayout.NORTH);
        top.add(getSearchPanel(), BorderLayout.SOUTH);

        return top;
    }

    private JPanel getButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(ColorsStorage.backgroundColor);

        JButton addBtn = new JButton("Новый заказ");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");

        addBtn.addActionListener(e -> openForm(null));
        editBtn.addActionListener(e -> {
            Order selected = getSelectedOrder();
            if (selected != null) {
                openForm(selected);
            }
        });
        deleteBtn.addActionListener(e -> deleteSelected());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        return panel;
    }

    private JPanel getSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(ColorsStorage.backgroundColor);

        searchCombo = new JComboBox<>(new String[]{"Мастеру", "Дате", "Статусу"});
        searchField = new JTextField(16);
        statusCombo = new JComboBox<>(Status.values());
        statusCombo.setVisible(false);

        searchCombo.addActionListener(e -> {
            boolean isStatus = searchCombo.getSelectedIndex() == 2;
            searchField.setVisible(!isStatus);
            statusCombo.setVisible(isStatus);
        });

        JButton searchBtn = new JButton("Найти");
        JButton resetBtn = new JButton("Сбросить");

        searchBtn.addActionListener(e -> doSearch());
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            loadTable(orderDAO.findAll());
        });

        panel.add(new JLabel("Поиск по:"));
        panel.add(searchCombo);
        panel.add(searchField);
        panel.add(statusCombo);
        panel.add(searchBtn);
        panel.add(resetBtn);

        return panel;
    }

    private JScrollPane getTable() {
        String[] columns = {"ID", "Автомобиль", "Описание", "Мастер", "Стоимость", "Дата приёма", "Дата готовности", "Статус"};
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

    private void loadTable(List<Order> orders) {
        tableModel.setRowCount(0);
        for (Order o : orders) {
            tableModel.addRow(new Object[]{o.getId(), o.getCarInfo(), o.getDescription(), o.getMasterName(), o.getCost(),
                    o.getDateCreated(), o.getDateCompleted(), o.getStatus().name()});
        }
    }

    private void doSearch() {
        int idx = searchCombo.getSelectedIndex();
        List<Order> result;

        if (idx == 0) {
            result = orderDAO.searchByMaster(searchField.getText().trim());
        } else if (idx == 1) {
            result = orderDAO.searchByDate(searchField.getText().trim());
        } else {
            result = orderDAO.searchByStatus((Status) statusCombo.getSelectedItem());
        }

        loadTable(result);
    }

    private void openForm(Order order) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        OrderFormDialog dialog = new OrderFormDialog(parent, order, orderDAO, carDAO);
        dialog.setVisible(true);
        loadTable(orderDAO.findAll());
    }

    private void deleteSelected() {
        Order order = getSelectedOrder();
        if (order == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Удалить заказ #" + order.getId() + "?",
                "Подтверждение", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            orderDAO.delete(order.getId());
            loadTable(orderDAO.findAll());
        }
    }

    private Order getSelectedOrder() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Выберите строку в таблице.");
            return null;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        for (Order o : orderDAO.findAll()){
            if (o.getId() == id) {
                return o;
            }
        }

        return null;
    }
}