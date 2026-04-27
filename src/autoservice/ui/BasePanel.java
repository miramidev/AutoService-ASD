package autoservice.ui;

import autoservice.helper.ColorsStorage;

import javax.swing.*;
import java.awt.*;

public class BasePanel extends JPanel {
    public BasePanel(String title)
    {
        setLayout(new BorderLayout());
        setBackground(ColorsStorage.backgroundColor);

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(ColorsStorage.defaultTextColor);

        add(label, BorderLayout.CENTER);
    }
}