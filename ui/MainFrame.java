package ui;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import model.MenuData;
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

    public List<MenuData> getMenuListFromDatabase() {
        // 假设你已经连接了数据库并执行了查询
        // 这里模拟从数据库中加载数据
        return Arrays.asList(
                new MenuData(1, "文件", 0, null),
                new MenuData(2, "新建", 1, "com.example.NewFileFunction"),
                new MenuData(3, "打开", 1, "com.example.OpenFileFunction"),
                new MenuData(4, "编辑", 0, null),
                new MenuData(5, "剪切", 4, "com.example.CutFunction"));
    }

    private void initMenuBar() {
        
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