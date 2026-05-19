package func;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ui.MainFrame;
import ui.frame.BaseFrame;
import ui.frame.InStockFrame;
import ui.frame.OutStockFrame;
import ui.panel.WarehouseTablePane;

public class WarehouseTableFunction extends AbstractButtonFunction {

    @Override
    public void executeFunction(JMenuItem src) {

        System.out.println("执行仓库表功能...");

        JPanel panel = new WarehouseTablePane();

        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                System.out.println("找到了 " + frames.length + " 个内部框架，正在显示仓库表...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof InStockFrame) {
                        ((InStockFrame) frame).bindWarehouseTablePane((WarehouseTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("展示仓库表并绑定监听器到InStockFrame！");
                        return;
                    }
                    if (frame instanceof OutStockFrame) {
                        ((OutStockFrame) frame).bindWarehouseTablePane((WarehouseTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("展示仓库表并绑定监听器到OutStockFrame！");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("展示仓库表！");
                        return;
                    }
                }
            }
        }
    }
}

