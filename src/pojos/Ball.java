package pojos;

import java.awt.*;
import java.util.*;

public class Ball extends Rectangle {
    // Variables para el movimiento de la pelota
    Random random;
    public int xVelocity;
    public int yVelocity;
    int initialSpeed = 5;

    public Ball(int x, int y, int width, int height) {
        // Crear una nueva pelota con ciertos valores
        super(x, y, width, height);

        // Crear una dirección aleatoria para la pelota
        random = new Random();
        int randomXDirection = random.nextInt(2);
        if (randomXDirection == 0) {
            randomXDirection--;
        }
        setXDirection(randomXDirection * initialSpeed);

        int randomYDirection = random.nextInt(2);
        if (randomYDirection == 0) {
            randomYDirection--;
        }
        setYDirection(randomYDirection * initialSpeed);
    }

    public void setXDirection(int randomXDirection) {
        xVelocity = randomXDirection;
    }

    public void setYDirection(int randomYDirection) {
        yVelocity = randomYDirection;
    }

    // Mover la pelota
    public void move() {
        x += xVelocity;
        y += yVelocity;
    }

    // Dibujar la pelota
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, height, width);
    }
}