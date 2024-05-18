import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Bird bird;
    private java.util.List<Pipe> pipes;
    private Timer timer;
    private int score;
    private boolean gameOver;
    private int pipeGap;
    private int pipeWidth;

    public GamePanel() {
        this.bird = new Bird();
        this.pipes = new java.util.ArrayList<>();
        this.timer = new Timer(20, this);
        this.score = 0;
        this.gameOver = false;
        this.pipeGap = 200;
        this.pipeWidth = 50;

        setFocusable(true);
        addKeyListener(this);
        initPipes();
    }

    public void startGame() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            bird.update();
            updatePipes();
            checkCollisions();
            repaint();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        bird.draw(g);
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        //TODO: draw score and other UI components
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            bird.flap();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void checkCollisions() {
        Rectangle birdBounds = bird.getBounds();

        for (Pipe pipe : pipes) {
            if (pipe.getBounds().intersects (birdBounds)) {
                gameOver = true;
                timer.stop();
            }

            if (!pipe.isScored() && pipe.getBounds().x + pipe.getBounds().width < birdBounds.x) {
                score++;
                pipe.setScored(true);
            }
        }

        if (bird.getBounds().y == 0 || bird.getBounds().y == 550) {
            gameOver = true;
            timer.stop();
        }
    }

    private void initPipes() {
        int pipeGap = 200;
        int pipeWidth = 50;
        for (int i = 0; i < 5; i++) {
            int pipeHeight = (int) (Math.random() * 200) + 100;
            pipes.add(new Pipe(800 + i * 300, 0, pipeWidth, pipeHeight));
            pipes.add(new Pipe(800 + i * 300, pipeHeight + pipeGap, pipeWidth,
                    600 - pipeHeight - pipeGap));
        }
    }

    private void updatePipes() {
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.update();
            if (pipe.isOffScreen()) {
                pipes.remove(i);
                i--; // Adjust the index after removal
                int pipeHeight = (int) (Math.random() * 200) + 100;
                pipes.add(new Pipe(800, 0, pipeWidth, pipeHeight));
                pipes.add(new Pipe(800, pipeHeight + pipeGap, pipeWidth, 600 - pipeHeight - pipeGap));
            }
        }
    }

}
