package func;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import ui.MainFrame;
import ui.frame.OutStockFrame;

public class OutStockFunction extends AbstractButtonFunction {

    @Override
    public void executeFunction(JMenuItem src) {

        System.out.println("执行出库功能...");

        // 先尝试找到已有的出库窗口
        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            // 遍历已有的InternalFrame
            for (JInternalFrame frame : desktopPane.getAllFrames()) {
                if (frame instanceof OutStockFrame) {
                    // 已存在，激活并返回
                    try {
                        frame.setSelected(true);
                    } catch (java.beans.PropertyVetoException e) {
                        // ignore
                    }
                    return;
                }
            }

            // 不存在则新建，并添加到desktopPane
            OutStockFrame newFrame = new OutStockFrame();
            desktopPane.add(newFrame);
            try {
                newFrame.setMaximum(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}