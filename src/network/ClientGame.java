package network;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import game.SetFrame;
import config.Config;

public class ClientGame {
    public static void main(String[] args) {
        Config config = new Config();
        String host = config.getString("socket_host");
        int port = config.getInt("socket_port");

        SetFrame frame = new SetFrame(false);

        try (Socket socket = new Socket(host, port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            while (true) {
                try {
                    frame.panel.paddle1.y = in.readInt();
                    frame.panel.ball.x = in.readInt();
                    frame.panel.ball.y = in.readInt();
                    frame.panel.score.player1 = in.readInt();
                    frame.panel.score.player2 = in.readInt();

                    out.writeInt(frame.panel.paddle2.y);
                    out.flush();
                    frame.panel.repaint();
                } catch (IOException e) {
                    handleDisconnection(frame, "El jugador 1 se ha desconectado.");
                    break; // Salir del bucle para cerrar el cliente
                }
            }
        } catch (IOException e) {
            handleDisconnection(frame, "No se pudo conectar al servidor.");
        }
    }

    private static void handleDisconnection(SetFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Jugador desconectado", JOptionPane.ERROR_MESSAGE);
        frame.dispose(); // Cerrar la ventana del juego
        System.exit(0); // Salir del programa
    }
}