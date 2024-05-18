import java.awt.*;

public class Bird {
    private int x, y;
    private int width, height;
    private int yVelocity;

    public Bird() {
        this.x = 100;
        this.y = 250;
        this.width = 40;
        this.height = 30;
        this.yVelocity = 0;
    }

    public void update() {
        yVelocity += 1; // gravity
        y += yVelocity;

        if (y > 550) {
            y = 550;
            yVelocity = 0;
        }
    }

    public void flap() {
        yVelocity = -10;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
