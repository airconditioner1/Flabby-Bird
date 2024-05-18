import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

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

    private void addPipePair(int x) {
        int pipeHeight = (int) (Math.random() * 200) + 100;
        pipes.add(new Pipe(x, 0, pipeWidth, pipeHeight)); // Top pipe
        pipes.add(new Pipe(x, pipeHeight + pipeGap, pipeWidth, 600 - pipeHeight - pipeGap)); // Bottom pipe
    }


    private void initPipes() {
        pipes.clear(); // Clear any existing pipes
        int initialPipeX = 800; // Starting X position for the first set of pipes
        for (int i = 0; i < 5; i++) {
            addPipePair(initialPipeX + i * 300);
        }
    }

    private void updatePipes() {
        Iterator<Pipe> it = pipes.iterator();
        while (it.hasNext()) {
            Pipe pipe = it.next();
            pipe.update();
            if (pipe.isOffScreen()) {
                it.remove();
            }
        }

        // Ensure pipes are added in pairs with a consistent gap
        while (pipes.size() < 10) {
            int lastPipeX = pipes.get(pipes.size() - 1).getX();
            addPipePair(lastPipeX + 300);
        }
    }


}
