package func;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class ChangePasswordFunction extends AbstractButtonFunction {

    private static final Preferences PREFS = Preferences.userNodeForPackage(ChangePasswordFunction.class);
    private static final String PASSWORD_KEY = "password";
    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    public void executeFunction(JMenuItem src) {
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Old password:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("New password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm password:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(src, panel, "Change Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            clear(oldPasswordField, newPasswordField, confirmPasswordField);
            return;
        }

        char[] oldPassword = oldPasswordField.getPassword();
        char[] newPassword = newPasswordField.getPassword();
        char[] confirmPassword = confirmPasswordField.getPassword();

        try {
            String currentPassword = PREFS.get(PASSWORD_KEY, DEFAULT_PASSWORD);
            if (!Arrays.equals(oldPassword, currentPassword.toCharArray())) {
                JOptionPane.showMessageDialog(src, "Old password is incorrect.", "Change Password",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (newPassword.length == 0) {
                JOptionPane.showMessageDialog(src, "New password cannot be empty.", "Change Password",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!Arrays.equals(newPassword, confirmPassword)) {
                JOptionPane.showMessageDialog(src, "The two new passwords do not match.", "Change Password",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            PREFS.put(PASSWORD_KEY, new String(newPassword));
            JOptionPane.showMessageDialog(src, "Password changed successfully.", "Change Password",
                    JOptionPane.INFORMATION_MESSAGE);
        } finally {
            Arrays.fill(oldPassword, '\0');
            Arrays.fill(newPassword, '\0');
            Arrays.fill(confirmPassword, '\0');
            clear(oldPasswordField, newPasswordField, confirmPasswordField);
        }
    }

    private void clear(JPasswordField... fields) {
        for (JPasswordField field : fields) {
            char[] password = field.getPassword();
            Arrays.fill(password, '\0');
            field.setText("");
        }
    }
}
