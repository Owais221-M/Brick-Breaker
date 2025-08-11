// GameWindow.java

import javax.swing.*;

public class GameWindow extends JFrame {
    public GameWindow(int level) {
        setTitle("Brick Breaker Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Add the BrickBreakerGame panel with the selected level
        BrickBreakerGame gamePanel = new BrickBreakerGame(level);
        add(gamePanel);

        // Center the window on screen
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
}
