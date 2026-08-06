package wartimeinfosystem.ui;

import wartimeinfosystem.controllers.EncryptionController;
import wartimeinfosystem.controllers.MessageController;
import wartimeinfosystem.enums.Appointment;
import wartimeinfosystem.enums.EncryptionType;
import wartimeinfosystem.models.Message;
import wartimeinfosystem.models.User;
import wartimeinfosystem.utils.SystemValidator;
import wartimeinfosystem.utils.UIHelper;
import wartimeinfosystem.utils.BackgroundPanel;
import wartimeinfosystem.controllers.ApplicationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Sender interface for encrypting and sending messages
 */
public class SenderFrame extends JFrame {
    private User currentUser;
    private JTextArea messageArea;
    private JComboBox<EncryptionType> encryptionCombo;
    private JComboBox<Appointment> appointmentCombo;
    private JTextField keyField;
    private JButton sendButton;
    private JButton returnButton;
    private JButton exitButton;
    
    private EncryptionController encryptionController;
    
    public SenderFrame(User user) {
        this.currentUser = user;
        this.encryptionController = new EncryptionController();
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initializeComponents() {
        setTitle("Send Message - War-Time Information System");
        setSize(800, 650);
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
        titlePanel.setPreferredSize(new Dimension(800, 80));
        
        JLabel titleLabel = new JLabel("SEND ENCRYPTED MESSAGE");
        titleLabel.setFont(UIHelper.getMilitaryFont(26));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Main Panel with semi-transparent background
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(210, 180, 140, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Message Text
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel messageLabel = new JLabel("Message Text:");
        messageLabel.setFont(UIHelper.getMilitaryFont(14));
        messageLabel.setForeground(Color.WHITE);
        formPanel.add(messageLabel, gbc);
        
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        messageArea = new JTextArea(10, 40);
        messageArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        formPanel.add(scrollPane, gbc);
        
        // Encryption Key
        gbc.gridy = 2;
        gbc.weighty = 0;
        JLabel keyLabel = new JLabel("Encryption Key:");
        keyLabel.setFont(UIHelper.getMilitaryFont(14));
        keyLabel.setForeground(Color.WHITE);
        formPanel.add(keyLabel, gbc);
        
        gbc.gridy = 3;
        keyField = new JTextField(30);
        keyField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(keyField, gbc);
        
        // Encryption Method
        gbc.gridy = 4;
        JLabel encryptionLabel = new JLabel("Encryption Method:");
        encryptionLabel.setFont(UIHelper.getMilitaryFont(14));
        encryptionLabel.setForeground(Color.WHITE);
        formPanel.add(encryptionLabel, gbc);
        
        gbc.gridy = 5;
        encryptionCombo = new JComboBox<>(EncryptionType.values());
        encryptionCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(encryptionCombo, gbc);
        
        // Sender Appointment
        gbc.gridy = 6;
        JLabel appointmentLabel = new JLabel("Your Appointment (Sender Rank):");
        appointmentLabel.setFont(UIHelper.getMilitaryFont(14));
        appointmentLabel.setForeground(Color.WHITE);
        formPanel.add(appointmentLabel, gbc);
        
        gbc.gridy = 7;
        appointmentCombo = new JComboBox<>(Appointment.values());
        appointmentCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        appointmentCombo.setSelectedItem(currentUser.getAppointment());
        formPanel.add(appointmentCombo, gbc);
        
        // Send Button
        gbc.gridy = 8;
        sendButton = new JButton("ENCRYPT & SEND MESSAGE");
        sendButton.setFont(UIHelper.getMilitaryFont(18));
        sendButton.setPreferredSize(new Dimension(320, 55));
        sendButton.setBackground(UIHelper.DARK_OLIVE);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        formPanel.add(sendButton, gbc);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(75, 83, 32, 230));
        
        returnButton = new JButton("RETURN");
        returnButton.setFont(UIHelper.getMilitaryFont(16));
        returnButton.setPreferredSize(new Dimension(160, 45));
        returnButton.setBackground(UIHelper.DARK_OLIVE);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        
        exitButton = new JButton("EXIT");
        exitButton.setFont(UIHelper.getMilitaryFont(16));
        exitButton.setPreferredSize(new Dimension(160, 45));
        exitButton.setBackground(UIHelper.WARNING_RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        
        bottomPanel.add(returnButton);
        bottomPanel.add(exitButton);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToOptions();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    private void sendMessage() {
        String messageText = messageArea.getText();
        String key = keyField.getText();
        EncryptionType encType = (EncryptionType) encryptionCombo.getSelectedItem();
        Appointment appointment = (Appointment) appointmentCombo.getSelectedItem();
        
        // Validate
        if (!SystemValidator.validateMessage(messageText)) {
            UIHelper.showErrorDialog(this, "Please enter a message to send.");
            return;
        }
        
        if (!SystemValidator.validateKey(key)) {
            UIHelper.showErrorDialog(this, "Please enter a valid encryption key (minimum 3 characters).");
            return;
        }
        
        // Encrypt message
        String encryptedText = encryptionController.encryptMessage(messageText, key, encType);
        
        // Create message object
        Message message = new Message(messageText, encryptedText, key, encType, appointment, currentUser.getName());
        
        // Send message
        MessageController msgController = ApplicationController.getMessageController();
        msgController.sendMessage(message);
        
        UIHelper.showInfoDialog(this, "Message encrypted and sent successfully!");
        
        // Clear fields
        messageArea.setText("");
        keyField.setText("");
    }
    
    private void returnToOptions() {
        OptionsFrame optionsFrame = new OptionsFrame(currentUser);
        optionsFrame.setVisible(true);
        dispose();
    }
}
