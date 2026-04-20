package ui;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import dao.MenuDao;
import service.MenuService;
import model.MenuItem;
import ui.frame.InStockFrame;

public class MainFrame extends JFrame {

    private JDesktopPane desktopPane;

    private void initUI() {
        // Set up the main frame
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
        frame.setVisible(true);

    }

    private void initMenuBar() {

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
        initUI();
        initEvents();
        initData();
    }

}