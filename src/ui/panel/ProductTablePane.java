package ui.panel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.ProductDao;
import model.Product;

public class ProductTablePane extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private ProductDao dao = new ProductDao();

    public interface OnProductSelectedListener {
        void onProductSelected(Product product);
    }

    private OnProductSelectedListener selectedListener;

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

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && selectedListener != null) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        Product product = new Product();
                        product.setId((int) model.getValueAt(selectedRow, 0));
                        product.setCode((String) model.getValueAt(selectedRow, 1));
                        product.setName((String) model.getValueAt(selectedRow, 2));
                        product.setSpec((String) model.getValueAt(selectedRow, 3));
                        product.setType((String) model.getValueAt(selectedRow, 4));
                        product.setUnit((String) model.getValueAt(selectedRow, 5));
                        selectedListener.onProductSelected(product);
                    }
                }
            }
        });

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

    public void setOnProductSelectedListener(OnProductSelectedListener listener) {
        this.selectedListener = listener;
    }
}
