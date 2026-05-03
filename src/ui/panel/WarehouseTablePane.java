package ui.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.WarehouseDao;
import model.Warehouse;

public class WarehouseTablePane extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private WarehouseDao dao = new WarehouseDao();

    public WarehouseTablePane() {
        setLayout(new BorderLayout());
        initTable();
        loadData();
    }

    private void initTable() {

        String[] cols = {"ID", "仓库名称", "位置"};

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {

        List<Warehouse> list = dao.findAll();

        model.setRowCount(0);

        for (Warehouse w : list) {
            model.addRow(new Object[]{
                    w.getId(),
                    w.getName(),
                    w.getLocation()
            });
        }
    }

    public void refresh() {
        loadData();
    }
}

