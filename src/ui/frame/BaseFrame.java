package ui.frame;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import ui.MainFrame;

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

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT);

        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        splitPane.setDividerLocation(400);

        bottomPanel = new JPanel();

        this.add(splitPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    protected void setLeft(Component comp) {
        leftPanel.removeAll(); // 【改动1】
        leftPanel.add(comp, BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    protected void setRight(Component comp) {

        if (comp == null)
            return; // 【改动2：防止NPE】

        rightPanel.removeAll(); // 【改动3：关键，先清空】
        rightPanel.add(comp, BorderLayout.CENTER);

        rightPanel.revalidate(); // 【改动4：刷新UI】
        rightPanel.repaint();
    }

    protected void setBottom(Component comp) {
        bottomPanel.removeAll(); // 【改动5：统一风格】
        bottomPanel.add(comp);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    // 【改动6：提供外部调用入口（菜单用）】
    public void showRight(Component comp) {
        setRight(comp);
    }
}
