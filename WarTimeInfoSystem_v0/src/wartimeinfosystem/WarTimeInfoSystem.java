package wartimeinfosystem;

import wartimeinfosystem.ui.IntroductoryFrame;
import javax.swing.SwingUtilities;

/**
 * Main class to launch the War-Time Information Passing System
 * Demonstrates all OOP principles: Encapsulation, Inheritance, Polymorphism, Abstraction
 * 
 * @author War-Time Info System Team
 * @version 1.0
 */


public class WarTimeInfoSystem {
    
    public static void main(String[] args) {
        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IntroductoryFrame introFrame = new IntroductoryFrame();
                introFrame.setVisible(true);
            }
        });
    }
}
