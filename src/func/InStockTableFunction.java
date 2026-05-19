package func;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ui.MainFrame;
import ui.frame.BaseFrame;
import ui.frame.InStockFrame;
import ui.panel.StockInTablePane;

public class InStockTableFunction extends AbstractButtonFunction {

    @Override
    public void executeFunction(JMenuItem src) {

        System.out.println("执行入库表功能...");

        JPanel panel = new StockInTablePane();

        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                System.out.println("找到了 " + frames.length + " 个内部框架，正在显示入库表...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof InStockFrame) {
                        ((InStockFrame) frame).bindTablePane((StockInTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("入库表展示并且绑定监听器");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("入库表展示");
                        return;
                    }
                }
            }
        }
    }
}

