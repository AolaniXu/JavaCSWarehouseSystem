import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

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
        setJMenuBar(new JMenuBar());

        

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