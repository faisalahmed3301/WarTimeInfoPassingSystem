package wartimeinfosystem.ui;

import wartimeinfosystem.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog showing information about the application
 */
public class InfoDialog extends JDialog {
    
    public InfoDialog(Frame parent) {
        super(parent, "Application Information", true);
        initializeComponents();
    }
    
    private void initializeComponents() {
        setSize(500, 400);
        setLayout(new BorderLayout());
        setResizable(false);
        
        UIHelper.centerWindow(this);
        
        // Title
        JLabel titleLabel = new JLabel("About This Application", SwingConstants.CENTER);
        titleLabel.setFont(UIHelper.getMilitaryFont(20));
        titleLabel.setBackground(UIHelper.MILITARY_GREEN);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setPreferredSize(new Dimension(500, 50));
        
        // Info Text
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setMargin(new Insets(10, 10, 10, 10));
        
        String info = "WAR-TIME INFORMATION PASSING SYSTEM\n\n" +
                     "Purpose:\n" +
                     "• Secure message transmission using encryption\n" +
                     "• Confidential military communication\n" +
                     "• Access control based on military rank\n\n" +
                     "Features:\n" +
                     "• Multiple encryption algorithms\n" +
                     "• Rank-based message access control\n" +
                     "• Secure key-based decryption\n" +
                     "• No database required\n" +
                     "• Offline operation\n\n" +
                     "Security Levels:\n" +
                     "• Commander: Full access\n" +
                     "• Officer: Limited access\n" +
                     "• Soldier: Basic access\n\n" +
                     "Developed using Java OOP principles:\n" +
                     "Encapsulation, Inheritance, Polymorphism, Abstraction";
        
        infoArea.setText(info);
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        
        // OK Button
        JButton okButton = new JButton("OK");
        okButton.setFont(UIHelper.getMilitaryFont(14));
        okButton.setPreferredSize(new Dimension(100, 40));
        okButton.setBackground(UIHelper.DARK_OLIVE);
        okButton.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
