package ui.panel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.StockInDTO;
import model.StockInView;
import service.StockInService;
import service.StockInServiceImpl;

public class StockInTablePane extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private StockInService service = new StockInServiceImpl();

    // 行选择回调接口（使用 DTO，加载完整数据到表单）
    public interface OnRowSelectedListener {
        void onRowSelected(StockInDTO dto);
    }

    private OnRowSelectedListener rowSelectedListener;

    public StockInTablePane() {
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
                "供应商",
                "操作人",
                "状态",
                "业务时间"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // 表格不可编辑
            }
        };
        table = new JTable(model);

        // 添加行选择监听器
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {  // 防止重复触发
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0 && rowSelectedListener != null) {
                        // 获取选中行的 ID（第一列）
                        int id = (int) model.getValueAt(selectedRow, 0);
                        // 查询完整数据
                        StockInDTO dto = service.findById(id);
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

        List<StockInView> list = service.getStockInList();

        model.setRowCount(0);

        for (StockInView v : list) {
            model.addRow(new Object[] {
                    v.getId(),
                    v.getWarehouseId(),
                    v.getCreateTime(),
                    v.getInvoiceNo(),
                    v.getSupplier(),
                    v.getOperator(),
                    v.getStatus(),
                    v.getBizTime()
            });
        }
    }

    // 刷新表格数据
    public void refresh() {
        loadData();
    }

    // 设置行选择监听器
    public void setOnRowSelectedListener(OnRowSelectedListener listener) {
        this.rowSelectedListener = listener;
    }
}