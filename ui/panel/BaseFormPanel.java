package ui.panel;

import javax.swing.JPanel;

public abstract class BaseFormPanel extends JPanel {
    public abstract Object getFormData();
    public abstract void setFormData(Object data);
    public abstract void clearForm();
    
}
