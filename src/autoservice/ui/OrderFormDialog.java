package autoservice.ui;

import autoservice.dao.CarDAO;
import autoservice.dao.OrderDAO;
import autoservice.helper.ColorsStorage;
import autoservice.model.Car;
import autoservice.model.Order;
import autoservice.model.Order.Status;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderFormDialog extends JDialog {
    private final OrderDAO orderDAO;
    private final CarDAO carDAO;
    private final Order order;
    private JComboBox<String> carCombo;
    private JTextArea descArea;
    private JTextField masterField;
    private JTextField costField;
    private JTextField dateCreatedField;
    private JTextField dateCompletedField;
    private JComboBox<Status> statusCombo;
    private List<Car> cars;

    public OrderFormDialog(JFrame parent, Order order, OrderDAO orderDAO, CarDAO carDAO) {
        super(parent, order == null ? "Новый заказ" : "Редактировать заказ #" + order.getId(), true);
        this.orderDAO = orderDAO;
        this.carDAO = carDAO;
        this.order = order == null ? new Order(0, 0, "", "", "", 0, "", "", Status.ПРИНЯТ) : order;

        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        add(buildForm(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ColorsStorage.backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cars = carDAO.findAll();
        String[] carNames = new String[cars.size()];
        for (int i = 0; i < cars.size(); i++) {
            carNames[i] = cars.get(i).getBrand() + " " + cars.get(i).getModel() + " (" + cars.get(i).getLicensePlate() + ")";
        }

        carCombo = new JComboBox<>(carNames);
        if (order.getCarId() > 0) {
            for (int i = 0; i < cars.size(); i++) {
                if (cars.get(i).getId() == order.getCarId()) {
                    carCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        addRow(panel, gbc, "Автомобиль:", 0, carCombo);

        descArea = new JTextArea(order.getDescription(), 3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        addRow(panel, gbc, "Описание работ:", 1, descScroll);

        masterField = new JTextField(order.getMasterName(), 18);
        addRow(panel, gbc, "Мастер:", 2, masterField);

        costField = new JTextField(order.getCost() > 0 ? String.valueOf(order.getCost()) : "", 18);
        addRow(panel, gbc, "Стоимость:", 3, costField);

        dateCreatedField = new JTextField(order.getDateCreated(), 18);
        addRow(panel, gbc, "Дата приёма (ДД.ММ.ГГГГ):", 4, dateCreatedField);

        dateCompletedField = new JTextField(order.getDateCompleted(), 18);
        addRow(panel, gbc, "Дата готовности:", 5, dateCompletedField);

        statusCombo = new JComboBox<>(Status.values());
        statusCombo.setSelectedItem(order.getStatus());
        addRow(panel, gbc, "Статус:", 6, statusCombo);

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String labelText, int row, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(ColorsStorage.backgroundColor);

        JButton saveBtn = new JButton("Сохранить");
        JButton cancelBtn = new JButton("Отмена");

        saveBtn.addActionListener(_ -> doSave());
        cancelBtn.addActionListener(_ -> dispose());

        panel.add(saveBtn);
        panel.add(cancelBtn);

        return panel;
    }

    private void doSave() {
        if (cars.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Сначала добавьте автомобиль!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (masterField.getText().trim().isEmpty() || costField.getText().trim().isEmpty() || dateCreatedField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните все обязательные поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double cost;
        try {
            cost = Double.parseDouble(costField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Стоимость должна быть числом!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Car selectedCar = cars.get(carCombo.getSelectedIndex());

        order.setCarId(selectedCar.getId());
        order.setCarInfo(selectedCar.getBrand() + " " + selectedCar.getModel() + " (" + selectedCar.getLicensePlate() + ")");
        order.setDescription(descArea.getText().trim());
        order.setMasterName(masterField.getText().trim());
        order.setCost(cost);
        order.setDateCreated(dateCreatedField.getText().trim());
        order.setDateCompleted(dateCompletedField.getText().trim());
        order.setStatus((Status) statusCombo.getSelectedItem());

        orderDAO.save(order);
        dispose();
    }
}