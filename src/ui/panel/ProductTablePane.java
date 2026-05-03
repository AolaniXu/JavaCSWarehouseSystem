package ui.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.ProductDao;
import model.Product;

public class ProductTablePane extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private ProductDao dao = new ProductDao();

    public ProductTablePane() {
        setLayout(new BorderLayout());
        initTable();
        loadData();
    }

    private void initTable() {

        String[] cols = {"ID", "编号", "名称", "规格", "类型", "单位"};

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

        List<Product> list = dao.findAll();

        model.setRowCount(0);

        for (Product p : list) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getCode(),
                    p.getName(),
                    p.getSpec(),
                    p.getType(),
                    p.getUnit()
            });
        }
    }

    public void refresh() {
        loadData();
    }
}
