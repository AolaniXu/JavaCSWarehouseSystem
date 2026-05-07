package func;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ui.MainFrame;
import ui.frame.BaseFrame;
import ui.panel.ProductTreePane;

public class ProductTreeFunction extends AbstractButtonFunction {

    @Override
    public void executeFunction(JMenuItem src) {

        System.out.println("Executing ProductTreeFunction...");

        JPanel panel = new ProductTreePane();

        // 直接使用 MainFrame 的静态实例
        MainFrame mainFrame = MainFrame.instance;
        System.out.println("Found main frame: " + mainFrame);

        if (mainFrame != null) {
            // 从 MainFrame 获取 desktopPane
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            // 获取已打开的 InternalFrame
            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                // 找到第一个 BaseFrame（入库Frame）
                System.out.println("Found " + frames.length + " internal frames, trying to show product tree panel...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Product tree panel shown!");
                        return;
                    }
                }
            }
        }
    }
}
