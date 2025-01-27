package game;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SetFrame extends JFrame {
    public PingvsPong panel;

    public SetFrame(boolean isServer) {
        panel = new PingvsPong(isServer);
        Color color = Color.decode("#164620");
        this.add(panel);
        this.setTitle("PingvsPong! - LAN");
        this.setResizable(false);
        this.setBackground(color);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setWindowIcon();

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        // Manejo de cierre de ventana
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (panel.output != null) {
                        panel.output.writeBoolean(true); // Notificar desconexión
                        panel.output.flush();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setWindowIcon() {
        try {
            Image icon = ImageIO.read(getClass().getResourceAsStream("/resources/icons/icon.png"));
            this.setIconImage(icon);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar el ícono.");
        }
    }
}


