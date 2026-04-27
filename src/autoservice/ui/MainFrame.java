package autoservice.ui;

import autoservice.ui.panels.CarsPanel;
import autoservice.ui.panels.OrdersPanel;
import autoservice.ui.panels.StatsPanel;

import javax.swing.*;

import java.awt.*;

import static autoservice.helper.ColorsStorage.backgroundColor;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Автосервис");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);

        CreateTabs();
    }

    private void CreateTabs() {
        UIManager.put("TabbedPane.font", new Font("SansSerif", Font.BOLD, 13));
        UIManager.put("TabbedPane.tabInsets", new Insets(4, 40, 4, 40));

        var tabs = new JTabbedPane();

        tabs.addTab("Автомобили", new CarsPanel());
        tabs.addTab("Заказы", new OrdersPanel());
        tabs.addTab("Статистика", new StatsPanel());
        add(tabs, BorderLayout.CENTER);
    }
}