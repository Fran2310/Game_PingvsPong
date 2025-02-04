package pojos;

import java.awt.*;
import java.io.*;
import java.util.Objects;

public class Score extends Rectangle {
    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    public int player1;
    public int player2;
    private Font customFont;

    public Score(int GAME_WIDTH, int GAME_HEIGHT) {
        Score.GAME_WIDTH = GAME_WIDTH;
        Score.GAME_HEIGHT = GAME_HEIGHT;
        loadCustomFont();
    }

    private void loadCustomFont() {
        try {
            // Cargar la fuente desde un archivo
            InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/Nunito-Black.ttf");
            Objects.requireNonNull(fontStream, "El archivo de fuente no se encontró");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(60f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            // Usar una fuente predeterminada en caso de error
            customFont = new Font("Consolas", Font.PLAIN, 60);
        }
    }

    public void draw(Graphics g) {

        // Dibujar el puntaje en la pantalla
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.WHITE);
        g.setFont(customFont);

        // Dibujar la línea central
        Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{25}, 30);
        g2d.setStroke(dashed);
        g2d.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);

        // Margen
        int yMargin = 20;
        int yPos = 50 + yMargin;

        // Dibujar el puntaje con etiquetas de jugadores con diferentes tamaños de fuente
        g.setFont(customFont.deriveFont(30f)); // Tamaño más pequeño para "Jugador 1" y "Jugador 2"
        //g.setColor(Color.decode("#ff7d00"));
        g.drawString("Player 1", (GAME_WIDTH / 2) - 215, 60);
        //g.setColor(Color.decode("#eac102"));
        g.drawString("Player 2", (GAME_WIDTH / 2) + 95, 60);

        g.setFont(customFont); // Volver al tamaño original para el puntaje
        g.setColor(Color.decode("#ff7d00"));
        g.drawString(String.valueOf(player1 / 10) + String.valueOf(player1 % 10), (GAME_WIDTH / 2) - 85, yPos);
        g.setColor(Color.decode("#eac102"));
        g.drawString(String.valueOf(player2 / 10) + String.valueOf(player2 % 10), (GAME_WIDTH / 2) + 15, yPos);
    }
}
