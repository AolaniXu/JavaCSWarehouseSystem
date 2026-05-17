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

        System.out.println("Executing InStockTableFunction...");

        JPanel panel = new StockInTablePane();

        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                System.out.println("Found " + frames.length + " internal frames, showing stock in table...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof InStockFrame) {
                        ((InStockFrame) frame).bindTablePane((StockInTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Stock in table shown and listener bound!");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Stock in table shown!");
                        return;
                    }
                }
            }
        }
    }
}

