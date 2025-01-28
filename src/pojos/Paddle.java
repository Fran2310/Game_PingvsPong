package pojos;
import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle {
    // Variables para el movimiento de la raqueta
    int id;
    int yVelocity;
    int speed = 10;

    // Crear una nueva raqueta con ciertos valores
    public Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
        this.id = id;
    }

    // Detectar las teclas presionadas
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(-speed);
            move();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(speed);
            move();
        }
    }

    // Detectar las teclas que se dejan de presionar
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(0);
            move();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(0);
            move();
        }
    }

    // Método para cambiar la dirección vertical de la raqueta
    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    // Método para mover la raqueta
    public void move() {
        y += yVelocity;
    }

    // Dibujar las raquetas
    public void draw(Graphics g) {
        if (id == 1) {
            g.setColor(Color.decode("#ff7d00"));//naranja
        } else { 
            g.setColor(Color.decode("#eac102"));//amarillo
        }
        g.fillRect(x, y, width, height);
        } 
    }
}