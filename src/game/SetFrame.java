package game;
import java.awt.*;
import javax.swing.*;

public class SetFrame extends JFrame {
    public PingvsPong panel;

    public SetFrame(boolean isServer) {
        // Crear un nuevo frame de juego con ciertas configuraciones
        panel = new PingvsPong(isServer);
        this.add(panel);
        this.setTitle("PingvsPong! - LAN");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
