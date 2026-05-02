package ui.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.StockInView;
import service.StockInService;
import service.StockInServiceImpl;

public class StockInTablePane extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private StockInService service = new StockInServiceImpl();

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

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

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
}
