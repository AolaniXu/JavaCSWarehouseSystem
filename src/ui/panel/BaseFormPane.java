package ui.panel;

import javax.swing.JPanel;


public abstract class BaseFormPane<T> extends JPanel {

    public BaseFormPane() {

    }

    protected final void init() {
        initComponents();
        layoutComponents();
        
    }

    protected abstract void initComponents();
    protected abstract void layoutComponents();

  
    

    public abstract void reset();

    
    public abstract T getData();
}
