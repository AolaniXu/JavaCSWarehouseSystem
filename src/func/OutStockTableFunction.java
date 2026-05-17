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

        System.out.println("Executing OutStockTableFunction...");

        JPanel panel = new OutStockTablePane();

        MainFrame mainFrame = MainFrame.instance;
        if (mainFrame != null) {
            JDesktopPane desktopPane = mainFrame.getDesktopPane();

            JInternalFrame[] frames = desktopPane.getAllFrames();
            if (frames.length > 0) {
                System.out.println("Found " + frames.length + " internal frames, showing stock out table...");
                for (JInternalFrame frame : frames) {
                    if (frame instanceof OutStockFrame) {
                        ((OutStockFrame) frame).bindTablePane((OutStockTablePane) panel);
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Stock out table shown and listener bound!");
                        return;
                    }
                    if (frame instanceof BaseFrame) {
                        ((BaseFrame) frame).showRight(panel);
                        System.out.println("Stock out table shown!");
                        return;
                    }
                }
            }
        }
    }
}
