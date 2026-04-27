package autoservice.ui.panels;

import autoservice.dao.CarDAO;
import autoservice.dao.OrderDAO;
import autoservice.helper.ColorsStorage;
import autoservice.helper.UIHelper;
import autoservice.model.Order;
import autoservice.model.Order.Status;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatsPanel extends JPanel {
    private final OrderDAO orderDAO = new OrderDAO();
    private final CarDAO carDAO = new CarDAO();
    private JPanel contentPanel;

    public StatsPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorsStorage.backgroundColor);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setBackground(ColorsStorage.backgroundColor);
        JButton refreshBtn = UIHelper.createButton("Обновить", ColorsStorage.buttonRefresh);
        refreshBtn.addActionListener(e -> refresh());
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ColorsStorage.backgroundColor);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private void refresh() {
        contentPanel.removeAll();

        List<Order> orders = orderDAO.findAll();
        int totalCars = carDAO.findAll().size();
        int totalOrders = orders.size();
        double totalCost = 0;
        for (Order o : orders) totalCost += o.getCost();
        double avgCost = totalOrders > 0 ? totalCost / totalOrders : 0;

        int принят = 0, вРаботе = 0, готов = 0, выдан = 0;
        for (Order o : orders) {
            if (o.getStatus() == Status.ПРИНЯТ) {
                принят++;
            }
            if (o.getStatus() == Status.В_РАБОТЕ) {
                вРаботе++;
            }
            if (o.getStatus() == Status.ГОТОВ) {
                готов++;
            }
            if (o.getStatus() == Status.ВЫДАН) {
                выдан++;
            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 0, 2, 0);

        contentPanel.add(sectionLabel("Общая статистика"), gbc); gbc.gridy++;
        contentPanel.add(statRow("Автомобилей в базе:", String.valueOf(totalCars)), gbc); gbc.gridy++;
        contentPanel.add(statRow("Всего заказов:", String.valueOf(totalOrders)), gbc); gbc.gridy++;
        contentPanel.add(statRow("Общая выручка:", String.format("%.2f руб.", totalCost)), gbc); gbc.gridy++;
        contentPanel.add(statRow("Средний чек:", String.format("%.2f руб.", avgCost)), gbc); gbc.gridy++;

        contentPanel.add(Box.createVerticalStrut(16), gbc); gbc.gridy++;
        contentPanel.add(sectionLabel("По статусам"), gbc); gbc.gridy++;
        contentPanel.add(statRow("Принят:",   String.valueOf(принят)),   gbc); gbc.gridy++;
        contentPanel.add(statRow("В работе:", String.valueOf(вРаботе)), gbc); gbc.gridy++;
        contentPanel.add(statRow("Готов:",    String.valueOf(готов)),    gbc); gbc.gridy++;
        contentPanel.add(statRow("Выдан:",    String.valueOf(выдан)),    gbc); gbc.gridy++;

        gbc.weighty = 1.0;
        contentPanel.add(new JPanel(), gbc);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(new Color(39, 110, 207));
        label.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private JPanel statRow(String key, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(220, 220, 220));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 0, 2, 0),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));

        JLabel keyLabel = new JLabel(key);
        keyLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        valueLabel.setForeground(new Color(30, 100, 200));

        row.add(keyLabel,   BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);

        return row;
    }
}