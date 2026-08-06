package wartimeinfosystem.ui;

import wartimeinfosystem.controllers.ApplicationController;
import wartimeinfosystem.controllers.MessageController;
import wartimeinfosystem.models.Message;
import wartimeinfosystem.models.User;
import wartimeinfosystem.utils.MessageFormatter;
import wartimeinfosystem.utils.UIHelper;
import wartimeinfosystem.utils.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Receiver interface for viewing and decrypting messages
 */
public class ReceiverFrame extends JFrame {
    private User currentUser;
    private JPanel messagesPanel;
    private JButton returnButton;
    private JButton exitButton;
    private JButton refreshButton;
    
    private MessageController messageController;
    
    public ReceiverFrame(User user) {
        this.currentUser = user;
        this.messageController = ApplicationController.getMessageController();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadMessages();
    }
    
    private void initializeComponents() {
        setTitle("Receive Messages - War-Time Information System");
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
        
        JLabel titleLabel = new JLabel("INCOMING MESSAGES");
        titleLabel.setFont(UIHelper.getMilitaryFont(26));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Messages Panel with semi-transparent background
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(new Color(210, 180, 140, 180));
        
        JScrollPane scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(75, 83, 32, 230));
        
        refreshButton = new JButton("REFRESH");
        refreshButton.setFont(UIHelper.getMilitaryFont(16));
        refreshButton.setPreferredSize(new Dimension(160, 45));
        refreshButton.setBackground(new Color(100, 120, 60));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        
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
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(returnButton);
        bottomPanel.add(exitButton);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMessages();
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
    
    private void loadMessages() {
        messagesPanel.removeAll();
        
        List<Message> messages = messageController.getAccessibleMessages(currentUser);
        
        if (messages.isEmpty()) {
            JLabel noMessagesLabel = new JLabel("NO OPERATIONAL INSTRUCTIONS PASSED YET");
            noMessagesLabel.setFont(UIHelper.getMilitaryFont(16));
            noMessagesLabel.setForeground(UIHelper.WARNING_RED);
            noMessagesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            messagesPanel.add(Box.createVerticalStrut(50));
            messagesPanel.add(noMessagesLabel);
        } else {
            for (Message message : messages) {
                JPanel messagePanel = createMessagePanel(message);
                messagesPanel.add(messagePanel);
                messagesPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }
    
    private JPanel createMessagePanel(Message message) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(650, 80));
        panel.setPreferredSize(new Dimension(650, 80));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(UIHelper.DARK_OLIVE, 2));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel previewLabel = new JLabel(MessageFormatter.formatMessagePreview(message));
        previewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel maskedLabel = new JLabel("Message: " + message.getMaskedMessage());
        maskedLabel.setFont(UIHelper.getMilitaryFont(14));
        
        infoPanel.add(previewLabel);
        infoPanel.add(maskedLabel);
        
        JButton decryptButton = new JButton("DECRYPT");
        decryptButton.setFont(UIHelper.getMilitaryFont(12));
        decryptButton.setBackground(UIHelper.DARK_OLIVE);
        decryptButton.setForeground(Color.WHITE);
        
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDecryptDialog(message);
            }
        });
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(decryptButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void openDecryptDialog(Message message) {
        DecryptDialog dialog = new DecryptDialog(this, message, currentUser, messageController);
        dialog.setVisible(true);
        loadMessages(); // Refresh after dialog closes
    }
    
    private void returnToOptions() {
        OptionsFrame optionsFrame = new OptionsFrame(currentUser);
        optionsFrame.setVisible(true);
        dispose();
    }
}
