package func;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ui.MainFrame;
import ui.frame.BaseFrame;
import ui.frame.InStockFrame;
import ui.frame.OutStockFrame;
import ui.panel.ProductTreePane;

public class ProductTreeFunction extends AbstractButtonFunction {

    @Override
    public void executeFunction(JMenuItem src) {

        System.out.println("执行产品树功能...");

        JPanel panel = new ProductTreePane();

        // 直接使用MainFrame的静态实例
        MainFrame mainFrame = MainFrame.instance;
        System.out.println("Found main frame: " + mainFrame);

        if (mainFrame != null) {
            // 从MainFrame获取desktopPane
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            // 获取已打开的InternalFrame
            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                // 找到第一个BaseFrame（入库Frame）
                System.out.println("找到了 " + frames.length + " 个内部框架，正在显示产品树面板...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof InStockFrame) {
                        ((InStockFrame) frame).bindProductTreePane((ProductTreePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("产品树面板显示并绑定到入库窗口！");
                        return;
                    }
                    if (frame instanceof OutStockFrame) {
                        ((OutStockFrame) frame).bindProductTreePane((ProductTreePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("产品树面板显示并绑定到出库窗口！");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("产品树面板显示！");
                        return;
                    }
                }
            }
        }
    }
}
