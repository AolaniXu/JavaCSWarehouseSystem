package ui.panel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.WarehouseDao;
import model.Warehouse;

public class WarehouseTablePane extends JPanel {

    public interface OnWarehouseSelectedListener {
        void onWarehouseSelected(Warehouse warehouse);
    }

    private JTable table;
    private DefaultTableModel model;
    private WarehouseDao dao = new WarehouseDao();
    private OnWarehouseSelectedListener selectedListener;

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
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && selectedListener != null) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        Warehouse warehouse = new Warehouse();
                        warehouse.setId((int) model.getValueAt(selectedRow, 0));
                        warehouse.setName((String) model.getValueAt(selectedRow, 1));
                        warehouse.setLocation((String) model.getValueAt(selectedRow, 2));
                        selectedListener.onWarehouseSelected(warehouse);
                    }
                }
            }
        });

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

    public void setOnWarehouseSelectedListener(OnWarehouseSelectedListener listener) {
        this.selectedListener = listener;
    }
}

