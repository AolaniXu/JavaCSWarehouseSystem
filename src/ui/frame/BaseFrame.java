package ui.frame;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.Component;




public abstract class BaseFrame extends JInternalFrame {

    protected JPanel leftPanel;
    protected JPanel rightPanel;
    protected JPanel bottomPanel;

    public BaseFrame() {
        initLayout();
    }

    private void initLayout() {

        this.setLayout(new BorderLayout());

        // 左右分割
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT);

        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        splitPane.setDividerLocation(400);

        // 底部
        bottomPanel = new JPanel();

        this.add(splitPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    // ===== 提供插槽方法 =====

    protected void setLeft(Component comp) {
        leftPanel.add(comp, BorderLayout.CENTER);
    }

    protected void setRight(Component comp) {
        rightPanel.add(comp, BorderLayout.CENTER);
    }

    protected void setBottom(Component comp) {
        bottomPanel.add(comp);
    }
}
