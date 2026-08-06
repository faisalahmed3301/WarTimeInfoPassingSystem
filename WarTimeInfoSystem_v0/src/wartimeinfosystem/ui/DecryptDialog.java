package wartimeinfosystem.ui;

import wartimeinfosystem.controllers.EncryptionController;
import wartimeinfosystem.controllers.MessageController;
import wartimeinfosystem.enums.Appointment;
import wartimeinfosystem.models.Message;
import wartimeinfosystem.models.User;
import wartimeinfosystem.security.AccessControl;
import wartimeinfosystem.utils.MessageFormatter;
import wartimeinfosystem.utils.SystemValidator;
import wartimeinfosystem.utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for decrypting messages
 */
public class DecryptDialog extends JDialog {
    private Message message;
    private User currentUser;
    private MessageController messageController;
    
    private JTextField keyField;
    private JComboBox<Appointment> appointmentCombo;
    private JButton decryptButton;
    private JButton cancelButton;
    
    private EncryptionController encryptionController;
    
    public DecryptDialog(Frame parent, Message message, User currentUser, MessageController messageController) {
        super(parent, "Decrypt Message", true);
        this.message = message;
        this.currentUser = currentUser;
        this.messageController = messageController;
        this.encryptionController = new EncryptionController();
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setSize(450, 300);
        setLayout(new BorderLayout());
        setResizable(false);
        
        UIHelper.centerWindow(this);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(UIHelper.MILITARY_GREEN);
        titlePanel.setPreferredSize(new Dimension(450, 60));
        
        JLabel titleLabel = new JLabel("ENTER DECRYPTION CREDENTIALS");
        titleLabel.setFont(UIHelper.getMilitaryFont(16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIHelper.LIGHT_TAN);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Decryption Key
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel keyLabel = new JLabel("Decryption Key:");
        keyLabel.setFont(UIHelper.getMilitaryFont(14));
        formPanel.add(keyLabel, gbc);
        
        gbc.gridx = 1;
        keyField = new JTextField(20);
        keyField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(keyField, gbc);
        
        // Appointment
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel appointmentLabel = new JLabel("Your Appointment:");
        appointmentLabel.setFont(UIHelper.getMilitaryFont(14));
        formPanel.add(appointmentLabel, gbc);
        
        gbc.gridx = 1;
        appointmentCombo = new JComboBox<>(Appointment.values());
        appointmentCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        appointmentCombo.setSelectedItem(currentUser.getAppointment());
        formPanel.add(appointmentCombo, gbc);
        
        // Info Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><center>Access depends on sender's rank.<br>Key must match encryption key.</center></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        formPanel.add(infoLabel, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UIHelper.LIGHT_TAN);
        
        decryptButton = new JButton("DECRYPT");
        decryptButton.setFont(UIHelper.getMilitaryFont(14));
        decryptButton.setPreferredSize(new Dimension(120, 40));
        decryptButton.setBackground(UIHelper.DARK_OLIVE);
        decryptButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("CANCEL");
        cancelButton.setFont(UIHelper.getMilitaryFont(14));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        
        buttonPanel.add(decryptButton);
        buttonPanel.add(cancelButton);
        
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setupListeners();
    }
    
    private void setupListeners() {
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptDecryption();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        keyField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptDecryption();
            }
        });
    }
    
    private void attemptDecryption() {
        String key = keyField.getText();
        Appointment userAppointment = (Appointment) appointmentCombo.getSelectedItem();
        
        if (!SystemValidator.validateKey(key)) {
            UIHelper.showErrorDialog(this, "Please enter a valid decryption key.");
            return;
        }
        
        // Check appointment access
        AccessControl accessControl = new AccessControl(currentUser);
        currentUser.setAppointment(userAppointment);
        
        if (!accessControl.canAccessMessage(message)) {
            showAccessDeniedDialog();
            return;
        }
        
        // Verify key and decrypt
        if (!key.equals(message.getEncryptionKey())) {
            UIHelper.showErrorDialog(this, "Incorrect decryption key!");
            return;
        }
        
        // Decrypt message
        String decryptedText = encryptionController.decryptMessage(
            message.getEncryptedMessage(), 
            key, 
            message.getEncryptionType()
        );
        
        // Show decrypted message
        showDecryptedMessage(decryptedText);
        
        // Mark as read and remove
        messageController.markAsRead(message);
        
        dispose();
    }
    
    private void showAccessDeniedDialog() {
        JDialog dialog = new JDialog(this, "Access Denied", true);
        dialog.setSize(350, 150);
        dialog.setLayout(new BorderLayout());
        
        UIHelper.centerWindow(dialog);
        
        JPanel panel = new JPanel();
        panel.setBackground(UIHelper.WARNING_RED);
        
        JLabel label = new JLabel("ACCESS DENIED");
        label.setFont(UIHelper.getMilitaryFont(18));
        label.setForeground(Color.WHITE);
        panel.add(label);
        
        JPanel msgPanel = new JPanel();
        JLabel msgLabel = new JLabel("Your rank is insufficient to access this message.");
        msgLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        msgPanel.add(msgLabel);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(okButton);
        
        dialog.add(panel, BorderLayout.NORTH);
        dialog.add(msgPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showDecryptedMessage(String decryptedText) {
        JDialog dialog = new JDialog(this, "Decrypted Message", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());
        
        UIHelper.centerWindow(dialog);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(UIHelper.MILITARY_GREEN);
        JLabel titleLabel = new JLabel("MESSAGE DECRYPTED SUCCESSFULLY");
        titleLabel.setFont(UIHelper.getMilitaryFont(16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        JTextArea textArea = new JTextArea(MessageFormatter.formatDecryptedMessage(message, decryptedText));
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(UIHelper.getMilitaryFont(14));
        okButton.setBackground(UIHelper.DARK_OLIVE);
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(okButton);
        
        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
}
