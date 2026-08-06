package wartimeinfosystem.ui;

import wartimeinfosystem.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog shown when access is denied
 */
public class AccessDeniedDialog extends JDialog {
    
    public AccessDeniedDialog(Frame parent) {
        super(parent, "Access Denied", true);
        initializeComponents();
    }
    
    private void initializeComponents() {
        setSize(400, 200);
        setLayout(new BorderLayout());
        setResizable(false);
        
        UIHelper.centerWindow(this);
        
        // Warning Panel
        JPanel warningPanel = new JPanel();
        warningPanel.setBackground(UIHelper.WARNING_RED);
        warningPanel.setPreferredSize(new Dimension(400, 100));
        
        JLabel warningLabel = new JLabel("ACCESS UNAUTHORIZED");
        warningLabel.setFont(UIHelper.getMilitaryFont(20));
        warningLabel.setForeground(Color.WHITE);
        warningPanel.add(warningLabel);
        
        // Message Panel
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(Color.WHITE);
        
        JLabel messageLabel = new JLabel("<html><center>Invalid credentials or appointment mismatch.<br>Please try again.</center></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messagePanel.add(messageLabel);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(UIHelper.getMilitaryFont(14));
        okButton.setPreferredSize(new Dimension(100, 40));
        okButton.setBackground(UIHelper.DARK_OLIVE);
        okButton.setForeground(Color.WHITE);
        
        buttonPanel.add(okButton);
        
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        add(warningPanel, BorderLayout.NORTH);
        add(messagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
