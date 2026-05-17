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

        System.out.println("Executing ProductTableFunction...");

        JPanel panel = new ProductTablePane();

        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                System.out.println("Found " + frames.length + " internal frames, showing product table...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof InStockFrame) {
                        ((InStockFrame) frame).bindProductTablePane((ProductTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Product table shown and listener bound to InStockFrame!");
                        return;
                    }
                    if (frame instanceof OutStockFrame) {
                        ((OutStockFrame) frame).bindProductTablePane((ProductTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Product table shown and listener bound to OutStockFrame!");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Product table shown!");
                        return;
                    }
                }
            }
        }
    }
}