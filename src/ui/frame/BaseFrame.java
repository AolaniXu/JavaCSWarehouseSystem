package ui.frame;

import java.awt.BorderLayout;

import javax.swing.*;

import ui.componet.DataNavigator;
import ui.panel.BaseFormPanel;

public abstract class BaseFrame extends JInternalFrame {

    protected BaseFormPanel formPanel;
    protected JTable table;
    protected DataNavigator navigator;

    public BaseFrame(String title) {
        super(title, true, true, true, true);
        setSize(900, 600);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // 子类提供表单
        formPanel = createFormPanel();

        // 表格统一创建
        table = new JTable();

        // 导航条统一创建
        navigator = new DataNavigator();

        // 左右分割
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                formPanel,
                new JScrollPane(table)
        );

        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);
        add(navigator, BorderLayout.SOUTH);

        // 交给子类处理
        bindEvents();
        loadData();
    }

    // 子类必须实现的内容
    protected abstract BaseFormPanel createFormPanel();
    protected abstract void bindEvents();
    protected abstract void loadData();
}
