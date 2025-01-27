package network;

import java.io.*;
import java.net.*;

import game.SetFrame;
import config.Config;

public class ServerGame {
    public static void main(String[] args) {
        // Instancia de configuración
        Config config = new Config();

        // Obtener el puerto desde el archivo properties
        int port = config.getInt("socket_port");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("PingvsPong Server On... Esperando jugador");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Jugador ingresado... Iniciando partida");

            SetFrame frame = new SetFrame(true);

            try (DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                 DataInputStream in = new DataInputStream(clientSocket.getInputStream())) {

                frame.panel.output = out; // Asignar el output al panel

                while (true) {
                    try {
                        // Enviar paddle1 y ball data al cliente
                        out.writeInt(frame.panel.paddle1.y);
                        out.writeInt(frame.panel.ball.x);
                        out.writeInt(frame.panel.ball.y);
                        out.writeInt(frame.panel.score.player1);
                        out.writeInt(frame.panel.score.player2);

                        // Por defecto, no hay desconexión
                        out.writeBoolean(false);
                        out.flush();

                        // Recibir paddle2 data del cliente
                        frame.panel.paddle2.y = in.readInt();

                        // Actualizar el frame
                        frame.panel.repaint();
                    } catch (EOFException | SocketException e) {
                        System.out.println("El jugador se ha desconectado. Notificando...");
                        out.writeBoolean(true); // Notificar al cliente sobre la desconexión
                        out.flush();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                frame.dispose();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
