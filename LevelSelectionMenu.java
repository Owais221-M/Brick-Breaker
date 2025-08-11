// LevelSelectionMenu.java (Main Menu UI)
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LevelSelectionMenu extends JFrame implements ActionListener {
    private JButton easyButton, mediumButton, hardButton;
    private JLabel titleLabel;
    private JPanel panel;

    public LevelSelectionMenu() {
        setTitle("Brick Breaker - Select Difficulty");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        // Title Label
        titleLabel = new JLabel("BRICK BREAKER", JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 40));
        titleLabel.setForeground(Color.ORANGE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Panel with Buttons
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 15, 15));
        panel.setBackground(Color.BLACK);

        easyButton = new JButton("🟢 Easy Mode");
        mediumButton = new JButton("🟡 Medium Mode");
        hardButton = new JButton("🔴 Hard Mode");

        styleButton(easyButton);
        styleButton(mediumButton);
        styleButton(hardButton);

        easyButton.addActionListener(this);
        mediumButton.addActionListener(this);
        hardButton.addActionListener(this);

        panel.add(easyButton);
        panel.add(mediumButton);
        panel.add(hardButton);

        add(titleLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 26));
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 100, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int level = 1;
        if (e.getSource() == mediumButton) level = 2;
        if (e.getSource() == hardButton) level = 3;

        new GameWindow(level);
        dispose();
    }
}