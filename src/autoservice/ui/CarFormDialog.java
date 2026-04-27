package autoservice.ui;

import autoservice.dao.CarDAO;
import autoservice.helper.ColorsStorage;
import autoservice.model.Car;

import javax.swing.*;
import java.awt.*;

public class CarFormDialog extends JDialog {

    private final CarDAO carDAO;
    private final Car car;
    private JTextField brandField;
    private JTextField modelField;
    private JTextField plateField;
    private JTextField yearField;
    private JTextField ownerField;
    private JTextField phoneField;

    public CarFormDialog(JFrame parent, Car car, CarDAO carDAO) {
        super(parent, car == null ? "Добавить автомобиль" : "Редактировать автомобиль", true);
        this.carDAO = carDAO;
        this.car = car == null ? new Car(0, "", "", "", 0, "", "") : car;

        setSize(400, 340);
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

        brandField = addRow(panel, gbc, "Марка:", 0, car.getBrand());
        modelField = addRow(panel, gbc, "Модель:", 1, car.getModel());
        plateField = addRow(panel, gbc, "Гос. номер:", 2, car.getLicensePlate());
        yearField = addRow(panel, gbc, "Год:", 3, car.getYear() > 0 ? String.valueOf(car.getYear()) : "");
        ownerField = addRow(panel, gbc, "Владелец:", 4, car.getOwnerName());
        phoneField = addRow(panel, gbc, "Телефон:", 5, car.getOwnerPhone());

        return panel;
    }

    private JTextField addRow(JPanel panel, GridBagConstraints gbc,
                              String labelText, int row, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField field = new JTextField(value, 18);
        panel.add(field, gbc);
        return field;
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(ColorsStorage.backgroundColor);

        JButton saveBtn = new JButton("Сохранить");
        JButton cancelBtn = new JButton("Отмена");

        saveBtn.addActionListener(e -> doSave());
        cancelBtn.addActionListener(e -> dispose()); // dispose = закрыть диалог

        panel.add(saveBtn);
        panel.add(cancelBtn);
        return panel;
    }

    private void doSave() {
        // Валидация
        if (brandField.getText().trim().isEmpty() ||
                modelField.getText().trim().isEmpty() ||
                plateField.getText().trim().isEmpty() ||
                ownerField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Год должен быть числом!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        car.setBrand(brandField.getText().trim());
        car.setModel(modelField.getText().trim());
        car.setLicensePlate(plateField.getText().trim());
        car.setYear(year);
        car.setOwnerName(ownerField.getText().trim());
        car.setOwnerPhone(phoneField.getText().trim());

        carDAO.save(car);
        dispose();
    }
}