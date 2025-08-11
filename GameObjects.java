import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// GameObject class with Information Hiding
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

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Setters (protected)
    protected void setX(int x) { this.x = x; }
    protected void setY(int y) { this.y = y; }
    protected void setWidth(int width) { this.width = width; }
    protected void setHeight(int height) { this.height = height; }
}

// Ball class with Method Overloading
class Ball extends GameObject {
    private int dx, dy;
    private final int BALL_SIZE = 20;
    private Color color;

    public Ball(int x, int y, int level) {
        super(x, y, 20, 20);
        this.color = Color.RED;
        this.dx = 2 + level;
        this.dy = -2 - level;
    }

    // Overloaded constructor
    public Ball(int x, int y) {
        this(x, y, 1);  // default to level 1
    }

    public void update() {
        setX(getX() + dx);
        setY(getY() + dy);

        if (getX() <= 0 || getX() >= 780) dx = -dx;
        if (getY() <= 0) dy = -dy;
    }

    public void reverseY() {
        dy = -dy;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(getX(), getY(), BALL_SIZE, BALL_SIZE);
    }
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

    public void enlarge() {
        setWidth(getWidth() + 20);
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

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void destroy() {
        isDestroyed = true;
    }

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

// ExplodingBrick
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

// UnbreakableBrick
class UnbreakableBrick extends Brick {
    public UnbreakableBrick(int x, int y) {
        super(x, y, Color.GRAY);
    }

    @Override
    public void destroy() {
        System.out.println("🚫 This brick is unbreakable!");
    }
}

// BonusBrick (for Extensibility)
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

// Parametric Polymorphism — ScoreBoard<T>
class ScoreBoard<T> {
    private Map<T, Integer> scores = new HashMap<>();

    public void addScore(T entity, int points) {
        scores.put(entity, scores.getOrDefault(entity, 0) + points);
    }

    public int getScore(T entity) {
        return scores.getOrDefault(entity, 0);
    }

    public void printScores() {
        for (Map.Entry<T, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
