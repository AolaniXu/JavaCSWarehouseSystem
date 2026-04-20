package ui.frame;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ui.componet.DataNavigator;
import ui.panel.BaseFormPanel;
import ui.panel.InStockFormPanel;

public class InStockFrame extends BaseFrame {

    public InStockFrame() {
        super("入库管理");
    }

    @Override
    protected BaseFormPanel createFormPanel() {
        return new InStockFormPanel();
    }

    @Override
    protected void bindEvents() {
        navigator.setListener(new DataNavigator.Listener() {
            public void onNew() {
                formPanel.clearForm();
            }

            public void onSave() {
                // 入库保存逻辑
            }

            public void onAudit() {
                // 入库审核逻辑
            }

            public void onPrev() {}
            public void onNext() {}
        });
    }

    @Override
    protected void loadData() {
        // 加载入库数据到 table
    }
}