package wartimeinfosystem.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Helper class for UI operations
 */
public class UIHelper {
    
    public static final Color MILITARY_GREEN = new Color(75, 83, 32);
    public static final Color DARK_OLIVE = new Color(85, 107, 47);
    public static final Color LIGHT_TAN = new Color(210, 180, 140);
    public static final Color WARNING_RED = new Color(139, 0, 0);
    
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }
    
    public static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showInfoDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showWarningDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public static boolean showConfirmDialog(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    
    public static Font getMilitaryFont(int size) {
        return new Font("Courier New", Font.BOLD, size);
    }
}
