package network;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

import game.SetFrame;
import config.Config;

public class ClientGame {
    public static void main(String[] args) {
        // Instancia de configuración
        Config config = new Config();

        // Obtener el host y el puerto desde el archivo properties
        String host = config.getString("socket_host");
        int port = config.getInt("socket_port");

        SetFrame frame = new SetFrame(false);

        try (Socket socket = new Socket(host, port)) {
            // Crear input y output streams
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            frame.panel.output = out; // Asignar el output al panel

            while (true) {
                try {
                    // Recibir paddle1, ball y puntuaje data del servidor
                    frame.panel.paddle1.y = in.readInt();
                    frame.panel.ball.x = in.readInt();
                    frame.panel.ball.y = in.readInt();
                    frame.panel.score.player1 = in.readInt();
                    frame.panel.score.player2 = in.readInt();

                    // Verificar desconexión
                    if (in.readBoolean()) {
                        JOptionPane.showMessageDialog(frame, "El otro jugador se ha desconectado. El juego se cerrará.");
                        break;
                    }

                    // Enviar paddle2 data al servidor
                    out.writeInt(frame.panel.paddle2.y);
                    out.flush();

                    // Actualizar el panel
                    frame.panel.repaint();
                } catch (EOFException | SocketException e) {
                    JOptionPane.showMessageDialog(frame, "Se perdió la conexión con el servidor. Cerrando el juego.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            frame.dispose();
            System.exit(0);
        }
    }
}
