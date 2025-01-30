package network;

import config.Config;
import game.SetFrame;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class ClientGame {

    private static final int WINNING_SCORE = 5;
    private static volatile boolean gameEnded = false; // Variable para controlar el fin del juego

    public static void main(String[] args) {
        Config config = new Config();
        String host = config.getString("socket_host");
        int port = config.getInt("socket_port");

        SetFrame frame = new SetFrame(false);

        try (Socket socket = new Socket(host, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Hilo para recibir datos del servidor
            Thread receiveThread = new Thread(() -> {
                try {
                    while (!gameEnded) {
                        frame.panel.paddle1.y = in.readInt();
                        frame.panel.ball.x = in.readInt();
                        frame.panel.ball.y = in.readInt();
                        frame.panel.score.player1 = in.readInt();
                        frame.panel.score.player2 = in.readInt();

                        // Verificar si hay un ganador
                        if (frame.panel.score.player1 >= WINNING_SCORE || frame.panel.score.player2 >= WINNING_SCORE) {
                            gameEnded = true; // Termina el juego
                            String winner = (frame.panel.score.player1 >= WINNING_SCORE) ? "Jugador 1" : "Jugador 2";
                            JOptionPane.showMessageDialog(frame, "¡" + winner + " ha ganado!", "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
                            frame.dispose(); // Cerrar la ventana del juego
                        }

                        frame.panel.repaint(); // Actualizar la interfaz gráfica
                    }
                } catch (IOException e) {
                    if (!gameEnded) { // Solo muestra el mensaje si el juego no ha terminado
                        handleDisconnection(frame, "El servidor se ha desconectado.");
                    }
                }
            });
            receiveThread.start();

            // Enviar la posición de la paleta al servidor
            while (!gameEnded) {
                try {
                    out.writeInt(frame.panel.paddle2.y);
                    out.flush();
                    Thread.sleep(1); // Pequeña pausa para evitar sobrecargar el servidor
                } catch (IOException | InterruptedException e) {
                    if (!gameEnded) { // Solo muestra el mensaje si el juego no ha terminado
                        handleDisconnection(frame, "Error al enviar datos al servidor: " + e.getMessage());
                    }
                    gameEnded = true; // Asegura que se detengan ambos hilos
                }
            }
        } catch (IOException e) {
            handleDisconnection(frame, "No se pudo conectar al servidor.");
        }
    }

    private static void handleDisconnection(SetFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Error de conexión", JOptionPane.ERROR_MESSAGE);
        frame.dispose(); // Cerrar la ventana del juego
        System.exit(0); // Salir del programa
    }
}
