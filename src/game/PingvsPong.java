package game;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import pojos.Ball;
import pojos.Paddle;
import pojos.Score;
import config.Config;

public class PingvsPong extends JPanel implements Runnable {
    // Instancia de configuración
    private static Config config = new Config();
    
    // Variables para el juego
    static final int GAME_WIDTH = config.getInt("game_width");
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (5.0 / 9.0));  // Cálculo basado en GAME_WIDTH
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = config.getInt("ball_diameter");
    static final int PADDLE_WIDTH = config.getInt("paddle_width");
    static final int PADDLE_HEIGHT = config.getInt("paddle_height");

    // Instancias sin inicializar
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    public Paddle paddle1;
    public Paddle paddle2;
    public Ball ball;
    public Score score;
    boolean isServer;

    // Constructor para el panel de juego
    PingvsPong(boolean isServer) {
        // Crear un nuevo panel de juego con ciertas configuraciones
        this.isServer = isServer;
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    // Método para crear una nueva pelota
    public void newBall() {
        // Crear una nueva pelota en el centro de la pantalla (eje X) y en una posición aleatoria (eje Y)
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
    }

    // Método para crear nuevas raquetas
    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    // Método para pintar el panel de juego
    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    // Método para dibujar los elementos del juego
    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
        Toolkit.getDefaultToolkit().sync(); // Sincronizar la animación
    }

    // Método para mover los elementos del juego
    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollision() {
        // Rebotar la pelota en los extremos de la pantalla
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }

        // Rebotar la pelota en la raqueta del jugador 1
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // Aumentar la velocidad de la pelota (opcional)
            if (ball.yVelocity > 0) {
                ball.yVelocity++; // Aumentar la velocidad de la pelota (opcional)
            } else {
                ball.yVelocity--;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        // Rebotar la pelota en la raqueta del jugador 2
        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // Aumentar la velocidad de la pelota (opcional)
            if (ball.yVelocity > 0) {
                ball.yVelocity++; // Aumentar la velocidad de la pelota (opcional)
            } else {
                ball.yVelocity--;
            }
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        // Rebotar las raquetas en los extremos de la pantalla
        if (paddle1.y <= 0) {
            paddle1.y = 0;
        }
        if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
        if (paddle2.y <= 0) {
            paddle2.y = 0;
        }
        if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }

        // Punto para el jugador 2
        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
        }

        // Punto para el jugador 1
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
        }
    }

    // Método para correr el juego
    public void run() {
        // Loop principal del juego
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    // Clase para manejar los eventos del teclado (ActionListener)
    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (isServer) {
                paddle1.keyPressed(e);
            } else {
                paddle2.keyPressed(e);
            }
        }

        public void keyReleased(KeyEvent e) {
            if (isServer) {
                paddle1.keyReleased(e);
            } else {
                paddle2.keyReleased(e);
            }
        }
    }
}
