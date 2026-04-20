package ui;

import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ProductFrame extends JInternalFrame {
    private JTable productTable;
    private JTextField nameTextField;
    private JTextField specTextField;
    private JTextField unitTextField;

    private void initUI() {
        setSize(400, 300);
    }

    private void initEvents() {

    }

    private void initData() {

    }

    public ProductFrame() {
        super("产品管理", true, true, true, true);
        initUI();
        initEvents();
        initData();
    }

}
