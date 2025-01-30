package network;

import config.Config;
import game.SetFrame;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class ServerGame {

    private static final int WINNING_SCORE = 5; // Puntaje máximo para ganar
    public static void main(String[] args) {
        Config config = new Config();
        int port = config.getInt("socket_port");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("PingvsPong Server On... Esperando jugador");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Jugador ingresado... Iniciando partida");

            SetFrame frame = new SetFrame(true);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            while (true) {
                try {
                    out.writeInt(frame.panel.paddle1.y);
                    out.writeInt(frame.panel.ball.x);
                    out.writeInt(frame.panel.ball.y);
                    out.writeInt(frame.panel.score.player1);
                    out.writeInt(frame.panel.score.player2);
                    out.flush();

                    // Recibir datos del cliente
                    frame.panel.paddle2.y = in.readInt();

                        // Verificar si hay un ganador
                    if (frame.panel.score.player1 >= WINNING_SCORE) {
                        out.writeUTF("GAME_OVER:Player 1"); // Notificar que el jugador 1 ha ganado
                        out.flush();
                        handleGameOver(frame, "¡Jugador 1 ha ganado!");
                        break; // Salir del bucle
                    } else if (frame.panel.score.player2 >= WINNING_SCORE) {
                        out.writeUTF("GAME_OVER:Player 2"); // Notificar que el jugador 2 ha ganado
                        out.flush();
                        handleGameOver(frame, "¡Jugador 2 ha ganado!");
                        break; // Salir del bucle
                    }
                    frame.panel.repaint();
                } catch (IOException e) {
                    handleDisconnection(frame, "El jugador 2 se ha desconectado.");
                    break; // Salir del bucle para cerrar el servidor
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleDisconnection(SetFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Jugador desconectado", JOptionPane.ERROR_MESSAGE);
        frame.dispose(); // Cerrar la ventana del juego
        System.exit(0); // Salir del programa
    }

    private static void handleGameOver(SetFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose(); // Cerrar la ventana del juego
        System.exit(0); // Salir del programa
    }
    
}