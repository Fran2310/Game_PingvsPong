package game;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
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

        // Establecer el ícono de la ventana
        setWindowIcon();

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void setWindowIcon() {
        try {
            // Cargar el ícono desde el archivo
            Image icon = ImageIO.read(getClass().getResourceAsStream("/resources/icons/icon.png"));
            this.setIconImage(icon);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar el ícono.");
        }
    }
}

