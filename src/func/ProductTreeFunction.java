package func;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ui.frame.BaseFrame;
import ui.panel.ProductTreePanel;

public class ProductTreeFunction extends AbstractButtonFunction {

    @Override
    public void executeFunction(JMenuItem src) {

        JPanel panel = new ProductTreePanel();

        JInternalFrame frame =
                (JInternalFrame) SwingUtilities.getAncestorOfClass(
                        JInternalFrame.class,
                        src
                );

        if (frame instanceof BaseFrame) {
            ((BaseFrame) frame).showRight(panel);
        }
    }
}
