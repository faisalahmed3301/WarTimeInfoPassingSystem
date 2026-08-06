package wartimeinfosystem.ui;

import wartimeinfosystem.utils.UIHelper;
import wartimeinfosystem.utils.BackgroundPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Introductory interface of the application
 */
public class IntroductoryFrame extends JFrame {  //1
    private JButton infoButton;
    private JButton proceedButton;
    
    public IntroductoryFrame() {
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    // 1
    
    private void initializeComponents() {
        setTitle("War-Time Information Passing System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        UIHelper.centerWindow(this);
    }
    // 1
    
    private void setupLayout() {
        // Use background image for main panel
        BackgroundPanel mainPanel = new BackgroundPanel("/Users/macbookair/Downloads/WarTimeInfoSystem/build/classes/RESOURCES/intro_background.jpg", true);
        mainPanel.setLayout(new BorderLayout());
        
        // Title Panel with semi-transparent overlay
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(75, 83, 32, 220));
        titlePanel.setPreferredSize(new Dimension(800, 120));
        
        JLabel titleLabel = new JLabel("WAR-TIME INFORMATION");
        titleLabel.setFont(UIHelper.getMilitaryFont(32));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("PASSING SYSTEM");
        subtitleLabel.setFont(UIHelper.getMilitaryFont(32));
        subtitleLabel.setForeground(Color.WHITE);
        
        titlePanel.setLayout(new GridLayout(2, 1));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        // Info Button (top right)
        infoButton = new JButton("i");
        infoButton.setFont(new Font("Arial", Font.BOLD, 24));
        infoButton.setPreferredSize(new Dimension(60, 60));
        infoButton.setBackground(UIHelper.LIGHT_TAN);
        infoButton.setForeground(UIHelper.MILITARY_GREEN);
        infoButton.setFocusPainted(false);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.CENTER);
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        infoPanel.add(infoButton);
        topPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Center Panel with content
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        
        JLabel descLabel = new JLabel("<html><center><font size='5' color='white'>Secure Military Communication System</font><br><br><font size='4' color='#DDDDDD'>Classified Information Transmission</font></center></html>");
        descLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        centerPanel.add(descLabel);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        
        proceedButton = new JButton("PROCEED TO LOGIN");
        proceedButton.setFont(UIHelper.getMilitaryFont(18));
        proceedButton.setPreferredSize(new Dimension(280, 55));
        proceedButton.setBackground(UIHelper.DARK_OLIVE);
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setFocusPainted(false);
        proceedButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        buttonPanel.add(proceedButton);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    //1
    private void setupListeners() {
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInfoDialog();
            }
        });
        
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginPage();
            }
        });
    }
    
    // 1
    
    private void showInfoDialog() {
        InfoDialog dialog = new InfoDialog(this);
        dialog.setVisible(true);
    }
    
    private void openLoginPage() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        dispose();
    }
}

