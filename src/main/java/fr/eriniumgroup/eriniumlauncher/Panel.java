package fr.eriniumgroup.eriniumlauncher;

import fr.eriniumgroup.eriniumlauncher.utils.MicrosoftThread;
import fr.theshark34.swinger.animation.QueryLoopAction;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import fr.theshark34.swinger.animation.Animator;
import javafx.scene.transform.Rotate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static fr.eriniumgroup.eriniumlauncher.Frame.*;

public class Panel extends JPanel implements SwingerEventListener {

    private Image background = getImage("launcher.png");
    private STexturedButton play = new STexturedButton(getBufferedImage("play.png"), getBufferedImage("play_hover.png"));
    private STexturedButton microsoft = new STexturedButton(getBufferedImage("microsoft.png"), getBufferedImage("microsoft.png"));
    private Color color;

    public Panel() throws IOException {
        this.setLayout(null);

        play.setBounds(684, 364, 74, 74);
        play.addEventListener(this);
        this.add(play);

        microsoft.setBounds(250, 250, 100, 100);
        microsoft.addEventListener(this);
        this.add(microsoft);
    }
    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);

        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

        g.setColor(getRGBA(43, 43, 43, 250));
        g.fillRoundRect(117, 44, 497, 402, 20, 20);

        Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
        g.setColor(hexToColor("#BFCFFF"));
        g.setFont(titleFont);
        g.drawString("Erinium Launcher", 5, 30);
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == microsoft){
            //Thread t = new Thread(new MicrosoftThread());
            //t.start();
            Launcher.crack();
        } else if (swingerEvent.getSource() == play) {
            try {
                Launcher.update();
            } catch (Exception e) {
                Launcher.getReporter().catchError(e, "Impossible de mettre à jour le launcher");
            }

            try {
                Launcher.launch();
            } catch (Exception e) {
                Launcher.getReporter().catchError(e, "Impossible de lancer le jeu");
            }
        }
    }

    public Color getRGBA(int r, int g, int b, int alpha) {
        Color color = new Color(r, g, b, alpha);
        return color;
    }

    public static Color hexToColor(String hex) {
        // Vérifiez si la chaîne hexadécimale commence par "#"
        if (hex.startsWith("#")) {
            hex = hex.substring(1); // Supprime le "#"
        }

        // Vérifiez la longueur de la chaîne hexadécimale
        if (hex.length() == 6) { // Format "RRGGBB"
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new Color(r, g, b);
        } else if (hex.length() == 8) { // Format "AARRGGBB"
            int a = Integer.parseInt(hex.substring(0, 2), 16);
            int r = Integer.parseInt(hex.substring(2, 4), 16);
            int g = Integer.parseInt(hex.substring(4, 6), 16);
            int b = Integer.parseInt(hex.substring(6, 8), 16);
            return new Color(r, g, b, a);
        } else {
            throw new IllegalArgumentException("Format de couleur hexadécimale non pris en charge : " + hex);
        }
    }
}
