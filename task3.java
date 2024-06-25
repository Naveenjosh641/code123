import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeGame extends JFrame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int SCALE = 20;
    private static final int DELAY = 100;

    private Snake snake;
    private Food food;
    private boolean running = true;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        snakes = new Snake();
        food = new Food();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                snakes.setDirection(e.getKeyCode());
            }
        });

        setVisible(true);
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (snakes.isCollidingWithFood(food)) {
                snakes.grow();
                food.spawn();
            } else if (snakes.isCollidingWithSelf()) {
                running = false;
            }

            snakes.move();
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        snakes.draw(g);
        food.draw(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.run();
        });
    }
}

class Snake {
    private LinkedList<Point> body;
    private int direction;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(WIDTH / 2, HEIGHT / 2));
        direction = KeyEvent.VK_RIGHT;
    }

    public void setDirection(int dir) {
        if (dir == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
            direction = dir;
        } else if (dir == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
            direction = dir;
        } else if (dir == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
            direction = dir;
        } else if (dir == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
            direction = dir;
        }
    }

    public void move() {
        Point head = body.getFirst();
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y -= SCALE;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y += SCALE;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x -= SCALE;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x += SCALE;
                break;
        }

        body.addFirst(newHead);

        if (body.size() > 1) {
            body.removeLast();
        }
    }

    public void grow() {
        body.addLast(body.getLast());
    }

    public boolean isCollidingWithFood(Food food) {
        return body.getFirst().equals(food.getLocation());
    }

    public boolean isCollidingWithSelf() {
        for (int i = 1; i < body.size(); i++) {
            if (body.getFirst().equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        for (Point p : body) {
            g.setColor(Color.GREEN);
            g.fillRect(p.x, p.y, SCALE, SCALE);
        }
    }
}

class Food {
    private Point location;

    public Food() {
        spawn();
    }

    public void spawn() {
        int x = (int) (Math.random() * (WIDTH / SCALE)) * SCALE;
        int y = (int) (Math.random() * (HEIGHT / SCALE)) * SCALE;
        location = new Point(x, y);
    }

    public Point getLocation() {
        return location;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(location.x, location.y, SCALE, SCALE);
    }
}
