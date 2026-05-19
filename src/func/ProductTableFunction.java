package func;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ui.MainFrame;
import ui.frame.BaseFrame;
import ui.frame.InStockFrame;
import ui.frame.OutStockFrame;
import ui.panel.ProductTablePane;

public class ProductTableFunction extends AbstractButtonFunction {

    @Override
    public void executeFunction(JMenuItem src) {

        System.out.println("执行产品表功能...");

        JPanel panel = new ProductTablePane();

        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                System.out.println("找到了 " + frames.length + " 个内部框架，正在显示产品表...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof InStockFrame) {
                        ((InStockFrame) frame).bindProductTablePane((ProductTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("产品表展示并绑定到入库窗口！");
                        return;
                    }
                    if (frame instanceof OutStockFrame) {
                        ((OutStockFrame) frame).bindProductTablePane((ProductTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("产品表展示并绑定到出库窗口！");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("产品表展示！");
                        return;
                    }
                }
            }
        }
    }
}