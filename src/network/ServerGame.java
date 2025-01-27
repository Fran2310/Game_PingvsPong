package network;
import java.io.*;
import java.net.*;

import game.SetFrame;

public class ServerGame {
    public static void main(String[] args) {       
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            // Esperar a que un cliente se conecte
            System.out.println("El server se está ejecutando...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("¡Un cliente se ha conectado!");

            // Iniciar el frame del juego
            SetFrame frame = new SetFrame(true);
            
            // Crear los streams de entrada y salida
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            
            while (true) {
                // Enviar paddle1 y ball data al cliente
                out.writeInt(frame.panel.paddle1.y);
                out.writeInt(frame.panel.ball.x);
                out.writeInt(frame.panel.ball.y);
                out.writeInt(frame.panel.score.player1);
                out.writeInt(frame.panel.score.player2);
                out.flush();
                
                // Recibir paddle2 data del cliente
                frame.panel.paddle2.y = in.readInt();
                
                // Actualizar el frame
                frame.panel.repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}