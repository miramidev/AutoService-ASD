package autoservice.ui;

import autoservice.helper.ColorsStorage;

import javax.swing.*;
import java.awt.*;

import static autoservice.helper.ColorsStorage.backgroundColor;
import static autoservice.helper.ColorsStorage.defaultTextColor;

public class MainFrame extends JFrame {

    public MainFrame()
    {
        setTitle("Автосервис");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);

        CreateTabs();
    }

    private void CreateTabs()
    {
        var tabs = new JTabbedPane();
        tabs.setBackground(backgroundColor);
        tabs.setForeground(defaultTextColor);

        JPanel carsPanel = new JPanel();
        carsPanel.setBackground(ColorsStorage.backgroundColor);
        CreateTestTabView(carsPanel, "Автомобили");

        JPanel ordersPanel = new JPanel();
        ordersPanel.setBackground(backgroundColor);
        CreateTestTabView(ordersPanel, "Заказы");

        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(backgroundColor);
        CreateTestTabView(statsPanel, "Статистика");

        tabs.addTab("Автомобили", carsPanel);
        tabs.addTab("Заказы", ordersPanel);
        tabs.addTab("Статистика", statsPanel);
        add(tabs);
    }

    private void CreateTestTabView(JPanel panel, String title)
    {
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(ColorsStorage.defaultTextColor);
        panel.add(label);
    }
}
