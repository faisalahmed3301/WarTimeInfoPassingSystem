package wartimeinfosystem.ui;

import wartimeinfosystem.enums.Appointment;
import wartimeinfosystem.models.User;
import wartimeinfosystem.security.AuthenticationManager;
import wartimeinfosystem.utils.UIHelper;
import wartimeinfosystem.utils.BackgroundPanel;
import wartimeinfosystem.utils.SystemValidator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login interface
 */
public class LoginFrame extends JFrame {
    private JTextField nameField;
    private JComboBox<Appointment> appointmentCombo;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    private AuthenticationManager authManager;
    
    public LoginFrame() {
        authManager = new AuthenticationManager();
        initializeTestUsers();
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initializeTestUsers() {
        // Register test users
        User commander = new User("John", "1234", Appointment.COMMANDER);
        User officer = new User("Smith", "1234", Appointment.OFFICER);
        User soldier = new User("Dave", "1234", Appointment.SOLDIER);
        
        authManager.registerUser(commander);
        authManager.registerUser(officer);
        authManager.registerUser(soldier);
    }
    
    private void initializeComponents() {
        setTitle("Login - War-Time Information System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        UIHelper.centerWindow(this);
    }
    
    private void setupLayout() {
        // Use background image for main panel
        BackgroundPanel mainPanel = new BackgroundPanel("//RESOURCES//background01.jpg", true);
        mainPanel.setLayout(new BorderLayout());
        
        // Title Panel with semi-transparent overlay
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(75, 83, 32, 230));
        titlePanel.setPreferredSize(new Dimension(600, 80));
        
        JLabel titleLabel = new JLabel("MILITARY LOGIN");
        titleLabel.setFont(UIHelper.getMilitaryFont(26));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form Panel with semi-transparent background
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(210, 180, 140, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(UIHelper.getMilitaryFont(14));
        nameLabel.setForeground(Color.WHITE);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameField, gbc);
        
        // Appointment
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel appointmentLabel = new JLabel("Appointment:");
        appointmentLabel.setFont(UIHelper.getMilitaryFont(14));
        appointmentLabel.setForeground(Color.WHITE);
        formPanel.add(appointmentLabel, gbc);
        
        gbc.gridx = 1;
        appointmentCombo = new JComboBox<>(Appointment.values());
        appointmentCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(appointmentCombo, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(UIHelper.getMilitaryFont(14));
        passwordLabel.setForeground(Color.WHITE);
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);
        
        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        loginButton = new JButton("LOGIN");
        loginButton.setFont(UIHelper.getMilitaryFont(18));
        loginButton.setPreferredSize(new Dimension(220, 50));
        loginButton.setBackground(UIHelper.DARK_OLIVE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        formPanel.add(loginButton, gbc);
        
        // Hint Panel
        JPanel hintPanel = new JPanel();
        hintPanel.setBackground(new Color(75, 83, 32, 230));
        JLabel hintLabel = new JLabel("<html><center><font color='white'>Test Users: John/Smith/Dave | Password: 1234</font><br><font color='#DDDDDD'>Select appropriate appointment</font></center></html>");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        hintPanel.add(hintLabel);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(hintPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }
    
    private void performLogin() {
        String name = SystemValidator.sanitizeInput(nameField.getText());
        String password = new String(passwordField.getPassword());
        Appointment appointment = (Appointment) appointmentCombo.getSelectedItem();
        
        if (!SystemValidator.validateUsername(name)) {
            UIHelper.showErrorDialog(this, "Please enter a valid name (minimum 3 characters)");
            return;
        }
        
        if (!SystemValidator.validatePassword(password)) {
            UIHelper.showErrorDialog(this, "Please enter a valid password (minimum 4 characters)");
            return;
        }
        
        // Check authentication
        boolean authenticated = false;
        for (User user : authManager.getRegisteredUsers()) {
            if (user.getName().equals(name) && 
                user.getPassword().equals(password) && 
                user.getAppointment() == appointment) {
                authenticated = true;
                authManager.authenticate(name, password);
                break;
            }
        }
        
        if (authenticated) {
            User currentUser = authManager.getCurrentUser();
            OptionsFrame optionsFrame = new OptionsFrame(currentUser);
            optionsFrame.setVisible(true);
            dispose();
        } else {
            showAccessDeniedDialog();
        }
    }
    
    private void showAccessDeniedDialog() {
        AccessDeniedDialog dialog = new AccessDeniedDialog(this);
        dialog.setVisible(true);
    }
}
