package ui.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.InventoryDao;
import model.InventoryView;

public class InventoryTablePane extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private InventoryDao dao = new InventoryDao();

    public InventoryTablePane() {
        setLayout(new BorderLayout());
        initTable();
        loadData();
    }

    private void initTable() {

        String[] cols = {"ID", "商品编号", "商品名称", "仓库名称", "库存数量"};

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

        List<InventoryView> list = dao.findAllForTable();

        model.setRowCount(0);

        for (InventoryView v : list) {
            model.addRow(new Object[]{
                    v.getId(),
                    v.getProductCode(),
                    v.getProductName(),
                    v.getWarehouseName(),
                    v.getQuantity()
            });
        }
    }

    public void refresh() {
        loadData();
    }
}
