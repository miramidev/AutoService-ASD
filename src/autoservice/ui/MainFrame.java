package autoservice.ui;

import autoservice.ui.panels.CarsPanel;
import autoservice.ui.panels.OrdersPanel;

import javax.swing.*;

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
        var tabs = new JTabbedPane();

        tabs.addTab("Автомобили", new CarsPanel());
        tabs.addTab("Заказы", new OrdersPanel());
        add(tabs);
    }
}