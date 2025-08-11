import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

// Abstract GameObject class
abstract class GameObject {
    private int x, y, width, height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Graphics g);
    public abstract void update();

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    protected void setX(int x) { this.x = x; }
    protected void setY(int y) { this.y = y; }
    protected void setWidth(int width) { this.width = width; }
    protected void setHeight(int height) { this.height = height; }
}

// Entry Point
public class BrickBreaker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LevelSelectionMenu());
    }
}

// Level selection menu
class LevelSelectionMenu extends JFrame implements ActionListener {
    private JButton easyButton, mediumButton, hardButton;

    public LevelSelectionMenu() {
        setTitle("Select Difficulty");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        easyButton = new JButton("Easy");
        mediumButton = new JButton("Medium");
        hardButton = new JButton("Hard");

        easyButton.addActionListener(this);
        mediumButton.addActionListener(this);
        hardButton.addActionListener(this);

        add(easyButton);
        add(mediumButton);
        add(hardButton);

        setVisible(true);
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

// Game window
class GameWindow extends JFrame {
    public GameWindow(int level) {
        setTitle("Brick Breaker Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new BrickBreakerGame(level));
        setVisible(true);
    }
}

// Main game panel
class BrickBreakerGame extends JPanel implements ActionListener, KeyListener {
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private javax.swing.Timer timer;
    private int score = 0;
    private int lives = 3;
    private int level;
    private boolean gameOver = false;
    private boolean paused = false;
    private Random rand;

    public BrickBreakerGame(int level) {
        this.level = level;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.DARK_GRAY);
        startLevel();
        timer = new javax.swing.Timer(10, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
    }

    private void startLevel() {
        paddle = new Paddle(350, 550);
        int ballX = paddle.getX() + paddle.getWidth() / 2 - 10;
        int ballY = paddle.getY() - 20;
        ball = new Ball(ballX, ballY, level);

        bricks = new ArrayList<>();
        rand = new Random();
        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};

        int brickRows = level * 3;
        int startX = 20;
        int endX = 720;

        for (int i = startX; i <= endX; i += 70) {
            for (int j = 50; j <= 50 + brickRows * 30; j += 30) {
                int type = rand.nextInt(12);
                if (type == 0) {
                    bricks.add(new ExplodingBrick(i, j));
                } else if (type == 1) {
                    bricks.add(new UnbreakableBrick(i, j));
                } else if (type == 2) {
                    bricks.add(new BonusBrick(i, j));
                } else {
                    bricks.add(new Brick(i, j, colors[rand.nextInt(colors.length)]));
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over! Score: " + score, 250, 300);
            g.drawString("Press M to return to Menu | Press R to Restart", 150, 350);
        } else {
            // Polymorphism by inclusion (dynamic binding)
            try {
                GameObject demo = (score % 2 == 0) ? new Ball(50, 50, 1) : new Paddle(60, 60);
                demo.draw(g); // Method overridden at runtime
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            ball.draw(g);
            paddle.draw(g);
            for (Brick brick : bricks) brick.draw(g);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Lives: " + lives, 700, 30);
        g.drawString("Level: " + level, 350, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            ball.update();
            paddle.update();
            checkCollisions();
        }
        repaint();
    }

    private void checkCollisions() {
        if (ball.getY() + ball.getHeight() >= paddle.getY() &&
            ball.getY() + ball.getHeight() <= paddle.getY() + paddle.getHeight() &&
            ball.getX() + ball.getWidth() >= paddle.getX() &&
            ball.getX() <= paddle.getX() + paddle.getWidth()) {
            ball.reverseY();
            ball.setY(paddle.getY() - ball.getHeight());
        }

        for (Brick brick : bricks) {
            if (!brick.isDestroyed() &&
                ball.getY() <= brick.getY() + brick.getHeight() &&
                ball.getY() + ball.getHeight() >= brick.getY() &&
                ball.getX() + ball.getWidth() >= brick.getX() &&
                ball.getX() <= brick.getX() + brick.getWidth()) {

                brick.destroy();
                ball.reverseY();
                score += 10;

                if (brick instanceof BonusBrick) {
                    paddle.increaseWidth(20);
                }
                break;
            }
        }

        bricks.removeIf(Brick::isDestroyed);

        if (bricks.isEmpty()) {
            startLevel();
        }

        if (ball.getY() > 600) {
            lives--;
            if (lives == 0) {
                gameOver = true;
            } else {
                int ballX = paddle.getX() + paddle.getWidth() / 2 - 10;
                int ballY = paddle.getY() - 20;
                ball = new Ball(ballX, ballY, level);
            }
        }
    }

    private void restartGame() {
        score = 0;
        lives = 3;
        gameOver = false;
        paused = false;
        startLevel();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_M) {
                SwingUtilities.invokeLater(() -> new LevelSelectionMenu());
                SwingUtilities.getWindowAncestor(this).dispose();
            }
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) paddle.moveLeft();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) paddle.moveRight();
        if (e.getKeyCode() == KeyEvent.VK_P) paused = !paused;
    }

    @Override
    public void keyReleased(KeyEvent e) { paddle.stop(); }
    @Override
    public void keyTyped(KeyEvent e) {}
}

// Ball class
class Ball extends GameObject {
    private int dx, dy;
    private final int BALL_SIZE = 20;
    private Color color;

    public Ball(int x, int y, int level) {
        super(x, y, 20, 20);
        this.color = Color.RED;
        if (level == 1) { this.dx = 3; this.dy = -3; }
        else if (level == 2) { this.dx = 4; this.dy = -4; }
        else { this.dx = 6; this.dy = -6; }
    }

    public void update() {
        setX(getX() + dx);
        setY(getY() + dy);
        if (getX() <= 0 || getX() >= 780) dx = -dx;
        if (getY() <= 0) dy = -dy;
    }

    public void reverseY() { dy = -dy; }
    public void setY(int y) { super.setY(y); }
    public void draw(Graphics g) { g.setColor(color); g.fillOval(getX(), getY(), BALL_SIZE, BALL_SIZE); }
}

// Paddle class
class Paddle extends GameObject {
    private int dx;
    private final int SPEED = 7;
    private Color color;

    public Paddle(int x, int y) {
        super(x, y, 120, 15);
        this.color = Color.BLUE;
    }

    public void moveLeft() { dx = -SPEED; }
    public void moveRight() { dx = SPEED; }
    public void stop() { dx = 0; }
    public void update() {
        setX(getX() + dx);
        if (getX() < 0) setX(0);
        if (getX() > 680) setX(680);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 10, 10);
    }

    public void increaseWidth(int value) {
        setWidth(getWidth() + value);
    }
}

// Brick class
class Brick extends GameObject {
    private boolean isDestroyed = false;
    private Color color;

    public Brick(int x, int y, Color color) {
        super(x, y, 60, 20);
        this.color = color;
    }

    public boolean isDestroyed() { return isDestroyed; }
    public void destroy() { isDestroyed = true; }

    public void draw(Graphics g) {
        if (!isDestroyed) {
            g.setColor(color);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
    }

    public void update() {}
}

// Exploding brick subclass
class ExplodingBrick extends Brick {
    public ExplodingBrick(int x, int y) {
        super(x, y, Color.MAGENTA);
    }

    @Override
    public void destroy() {
        if (!isDestroyed()) {
            super.destroy();
            System.out.println("💥 Explosion! Nearby bricks are damaged!");
        }
    }
}

// Unbreakable brick subclass
class UnbreakableBrick extends Brick {
    public UnbreakableBrick(int x, int y) {
        super(x, y, Color.GRAY);
    }

    @Override
    public void destroy() {
        System.out.println("🚫 This brick is unbreakable!");
    }
}

// Bonus brick subclass
class BonusBrick extends Brick {
    public BonusBrick(int x, int y) {
        super(x, y, Color.PINK);
    }

    @Override
    public void destroy() {
        super.destroy();
        System.out.println("🎁 Bonus! Paddle size increased.");
    }
}



