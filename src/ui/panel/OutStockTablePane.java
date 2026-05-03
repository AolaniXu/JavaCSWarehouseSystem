package ui.panel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.StockOutDTO;
import model.StockOutView;
import service.StockOutService;
import service.StockOutServiceImpl;

public class OutStockTablePane extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private StockOutService service = new StockOutServiceImpl();

    // 行选择回调接口
    public interface OnRowSelectedListener {
        void onRowSelected(StockOutDTO dto);
    }

    private OnRowSelectedListener rowSelectedListener;

    public OutStockTablePane() {
        setLayout(new BorderLayout());
        initTable();
        loadData();
    }

    private void initTable() {

        String[] cols = {
                "ID",
                "仓库ID",
                "创建时间",
                "单号",
                "客户",
                "操作人",
                "状态",
                "业务时间"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);

        // 行选择监听器
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0 && rowSelectedListener != null) {
                        int id = (int) model.getValueAt(selectedRow, 0);
                        StockOutDTO dto = service.findById(id);
                        if (dto != null) {
                            rowSelectedListener.onRowSelected(dto);
                        }
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {

        List<StockOutView> list = service.getStockOutList();

        model.setRowCount(0);

        for (StockOutView v : list) {
            model.addRow(new Object[]{
                    v.getId(),
                    v.getWarehouseId(),
                    v.getCreateTime(),
                    v.getInvoiceNo(),
                    v.getCustomer(),
                    v.getOperator(),
                    v.getStatus(),
                    v.getBizTime()
            });
        }
    }

    public void refresh() {
        loadData();
    }

    public void setOnRowSelectedListener(OnRowSelectedListener listener) {
        this.rowSelectedListener = listener;
    }
}
