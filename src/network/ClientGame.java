package network;
import java.io.*;
import java.net.*;

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
            
            while (true) {
                // Recibir paddle1, ball y puntuaje data del server
                frame.panel.paddle1.y = in.readInt();
                frame.panel.ball.x = in.readInt();
                frame.panel.ball.y = in.readInt();
                frame.panel.score.player1 = in.readInt();
                frame.panel.score.player2 = in.readInt();
                
                // Enviar paddle2 data al server
                out.writeInt(frame.panel.paddle2.y);
                out.flush();
                
                // Actualizar el panel
                frame.panel.repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}