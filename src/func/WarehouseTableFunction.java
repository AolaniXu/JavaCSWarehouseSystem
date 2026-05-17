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

        System.out.println("Executing WarehouseTableFunction...");

        JPanel panel = new WarehouseTablePane();

        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                System.out.println("Found " + frames.length + " internal frames, showing warehouse table...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof InStockFrame) {
                        ((InStockFrame) frame).bindWarehouseTablePane((WarehouseTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Warehouse table shown and listener bound to InStockFrame!");
                        return;
                    }
                    if (frame instanceof OutStockFrame) {
                        ((OutStockFrame) frame).bindWarehouseTablePane((WarehouseTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Warehouse table shown and listener bound to OutStockFrame!");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Warehouse table shown!");
                        return;
                    }
                }
            }
        }
    }
}

