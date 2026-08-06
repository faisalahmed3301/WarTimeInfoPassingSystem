package wartimeinfosystem.ui;

import wartimeinfosystem.models.User;
import wartimeinfosystem.utils.UIHelper;
import wartimeinfosystem.utils.BackgroundPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Options page - choose to send or receive messages
 */
public class OptionsFrame extends JFrame {
    private User currentUser;
    private JButton sendButton;
    private JButton receiveButton;
    private JButton exitButton;
    
    public OptionsFrame(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initializeComponents() {
        setTitle("Options - War-Time Information System");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        UIHelper.centerWindow(this);
    }
    
    private void setupLayout() {
        // Use background image for main panel
        BackgroundPanel mainPanel = new BackgroundPanel("/Users/macbookair/Downloads/background01.jpg", true);
        mainPanel.setLayout(new BorderLayout());
        
        // Title Panel with semi-transparent overlay
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(75, 83, 32, 230));
        titlePanel.setPreferredSize(new Dimension(700, 100));
        titlePanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("MISSION CONTROL", SwingConstants.CENTER);
        titleLabel.setFont(UIHelper.getMilitaryFont(28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Logged in as: " + currentUser.getName() + " [" + currentUser.getAppointment() + "]", SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        userLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(userLabel, BorderLayout.SOUTH);
        
        // Options Panel with semi-transparent background
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBackground(new Color(210, 180, 140, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);
        gbc.fill = GridBagConstraints.BOTH;
        
        sendButton = new JButton("<html><center>SEND MESSAGE<br><small>(Encrypt & Transmit)</small></center></html>");
        sendButton.setFont(UIHelper.getMilitaryFont(18));
        sendButton.setPreferredSize(new Dimension(280, 110));
        sendButton.setBackground(UIHelper.DARK_OLIVE);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        receiveButton = new JButton("<html><center>DECRYPT MESSAGE<br><small>(Receive & Decrypt)</small></center></html>");
        receiveButton.setFont(UIHelper.getMilitaryFont(18));
        receiveButton.setPreferredSize(new Dimension(280, 110));
        receiveButton.setBackground(UIHelper.DARK_OLIVE);
        receiveButton.setForeground(Color.WHITE);
        receiveButton.setFocusPainted(false);
        receiveButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        optionsPanel.add(sendButton, gbc);
        
        gbc.gridy = 1;
        optionsPanel.add(receiveButton, gbc);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(75, 83, 32, 230));
        
        exitButton = new JButton("EXIT");
        exitButton.setFont(UIHelper.getMilitaryFont(16));
        exitButton.setPreferredSize(new Dimension(160, 45));
        exitButton.setBackground(UIHelper.WARNING_RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        
        bottomPanel.add(exitButton);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSenderFrame();
            }
        });
        
        receiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReceiverFrame();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    private void openSenderFrame() {
        SenderFrame senderFrame = new SenderFrame(currentUser);
        senderFrame.setVisible(true);
        dispose();
    }
    
    private void openReceiverFrame() {
        ReceiverFrame receiverFrame = new ReceiverFrame(currentUser);
        receiverFrame.setVisible(true);
        dispose();
    }
}
