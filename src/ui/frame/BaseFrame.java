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
        leftPanel.removeAll(); 
        leftPanel.add(comp, BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    protected void setRight(Component comp) {

        if (comp == null)
            return; 
        rightPanel.removeAll(); 
        rightPanel.add(comp, BorderLayout.CENTER);

        rightPanel.revalidate(); 
        rightPanel.repaint();
    }

    protected void setBottom(Component comp) {
        bottomPanel.removeAll(); 
        bottomPanel.add(comp);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    public void showRight(Component comp) {
        setRight(comp);
    }
}
