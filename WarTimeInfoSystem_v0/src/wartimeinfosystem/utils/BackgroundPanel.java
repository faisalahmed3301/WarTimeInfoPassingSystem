package wartimeinfosystem.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JPanel that displays a background image
 * Supports image scaling and maintain aspect ratio
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private boolean scaleToFit;
    
    /**
     * Creates a BackgroundPanel with the specified image path
     * @param imagePath Path to the image file
     */
    public BackgroundPanel(String imagePath) {
        this(imagePath, true);
    }
    
    /**
     * Creates a BackgroundPanel with the specified image path and scaling option
     * @param imagePath Path to the image file
     * @param scaleToFit Whether to scale image to fit the panel
     */
    public BackgroundPanel(String imagePath, boolean scaleToFit) {
        this.scaleToFit = scaleToFit;
        loadImage(imagePath);
        setLayout(new BorderLayout());
    }
    
    /**
     * Creates a BackgroundPanel with an existing Image
     * @param image The Image to use as background
     */
    public BackgroundPanel(Image image) {
        this(image, true);
    }
    
    /**
     * Creates a BackgroundPanel with an existing Image and scaling option
     * @param image The Image to use as background
     * @param scaleToFit Whether to scale image to fit the panel
     */
    public BackgroundPanel(Image image, boolean scaleToFit) {
        this.scaleToFit = scaleToFit;
        this.backgroundImage = image;
        setLayout(new BorderLayout());
    }
    
    private void loadImage(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                backgroundImage = icon.getImage();
            } else {
                System.err.println("Failed to load image: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            
            if (scaleToFit) {
                // Scale image to fit the panel
                Image scaledImage = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                g2d.drawImage(scaledImage, 0, 0, null);
            } else {
                // Draw image at original size, centered
                int x = (getWidth() - backgroundImage.getWidth(null)) / 2;
                int y = (getHeight() - backgroundImage.getHeight(null)) / 2;
                g2d.drawImage(backgroundImage, x, y, null);
            }
        }
    }
    
    /**
     * Set a new background image
     * @param imagePath Path to the new image
     */
    public void setBackgroundImage(String imagePath) {
        loadImage(imagePath);
        repaint();
    }
    
    /**
     * Set a new background image
     * @param image The new Image
     */
    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
        repaint();
    }
}

