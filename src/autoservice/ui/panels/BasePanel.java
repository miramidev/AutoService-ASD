package autoservice.ui.panels;

import autoservice.helper.ColorsStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected DefaultTableModel tableModel;
    protected JTable table;
    protected JComboBox<String> searchCombo;
    protected JTextField searchField;

    public BasePanel() {
        setLayout(new BorderLayout());
        setBackground(ColorsStorage.backgroundColor);

        add(getTopPanel(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
    }

    protected abstract String[] getColumns();

    protected abstract JPanel getButtonsPanel();

    protected abstract JPanel getSearchPanel();

    protected abstract void doSearch();

    protected abstract void loadTable();

    private JPanel getTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(ColorsStorage.backgroundColor);
        top.add(getButtonsPanel(), BorderLayout.NORTH);
        top.add(getSearchPanel(), BorderLayout.SOUTH);

        return top;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(getColumns(), 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setBackground(ColorsStorage.buttonReset);
        table.getTableHeader().setForeground(Color.WHITE);

        return new JScrollPane(table);
    }

    protected int getSelectedId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Выберите строку в таблице.");
            return -1;
        }

        return (int) tableModel.getValueAt(row, 0);
    }

    protected JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    protected int showDeleteConfirm(String message) {
        return JOptionPane.showConfirmDialog(
                this, message, "Подтверждение", JOptionPane.YES_NO_OPTION
        );
    }
}