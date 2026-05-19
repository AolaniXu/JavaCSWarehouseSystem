package ui;

import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;

import dao.MenuDao;
import service.MenuService;
import model.MenuItem;
import ui.frame.InStockFrame;

public class MainFrame extends JFrame {

    // 静态引用，供ProductTreeFunction等使用
    public static MainFrame instance;

    private JDesktopPane desktopPane;

    private void initUI() {
        // 基本设置
        setTitle("仓库管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and add the desktop pane
        desktopPane = new JDesktopPane();
        getContentPane().add(desktopPane);// setContentPane(desktopPane);

        // Set up the menu bar
        initMenuBar();

        // Test loading InStockFrame
        InStockFrame frame = new InStockFrame();
        desktopPane.add(frame);

        try {
            frame.setMaximum(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initMenuBar() { // 创建菜单栏

        MenuDao dao = new MenuDao();
        List<MenuItem> list = dao.findAllMenus();

        MenuService service = new MenuService();
        JMenuBar menuBar = service.buildMenuBar(list);

        System.out.println("menuBar created");
        setJMenuBar(menuBar);
    }

    private void initEvents() {

    }

    private void initData() {

    }

    public MainFrame() {

        instance = this; // 保存静态引用
        initUI();
        initEvents();
        initData();

    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

}