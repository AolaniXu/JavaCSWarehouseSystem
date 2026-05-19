package func;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ui.MainFrame;
import ui.frame.BaseFrame;
import ui.frame.OutStockFrame;
import ui.panel.OutStockTablePane;

public class OutStockTableFunction extends AbstractButtonFunction {

    @Override
    public void executeFunction(JMenuItem src) {

        System.out.println("执行出库表功能...");

        JPanel panel = new OutStockTablePane();

        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                System.out.println("找到了 " + frames.length + " 个内部框架，正在显示出库表...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof OutStockFrame) {
                        ((OutStockFrame) frame).bindTablePane((OutStockTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("出库表展示并且绑定监听器！");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("出库表展示！");
                        return;
                    }
                }
            }
        }
    }
}
