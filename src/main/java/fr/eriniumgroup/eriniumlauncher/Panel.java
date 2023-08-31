package fr.eriniumgroup.eriniumlauncher;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static fr.eriniumgroup.eriniumlauncher.Frame.*;

public class Panel extends JPanel implements SwingerEventListener {

    private Image background = getImage("launcher.png");
    private STexturedButton play = new STexturedButton(getBufferedImage("play.png"), getBufferedImage("play_hover.png"));
    private STexturedButton settings = new STexturedButton(getBufferedImage("settings.png"), getBufferedImage("settings_hover.png"));
    private STexturedButton home = new STexturedButton(getBufferedImage("home.png"), getBufferedImage("home_hover.png"));
    private STexturedButton logout = new STexturedButton(getBufferedImage("logout.png"), getBufferedImage("logout_hover.png"));
    private List<Component> sideBtn = new ArrayList<>();
    public static JFXPanel jfxPanel = new JFXPanel();
    private static WebView webView;
    private static WebEngine webEngine;

    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");

    public Panel() throws IOException {
        this.setLayout(null);

        close.setBounds(Frame.getInstance().getWidth() - 45, 0, 40, 40);
        close.addEventListener(this);
        this.add(close);

        hide.setBounds(close.getX() - 45, 20 - (17 / 2), 40, 17);
        hide.addEventListener(this);
        this.add(hide);

        settings.setBounds(5, Frame.getInstance().getHeight() / 2 - 30,60,60);
        settings.setLocation(5, Frame.getInstance().getHeight() / 2 /*((sideBtn.size() * 30) / 2)*/);
        settings.addEventListener(this);
        this.add(settings);

        home.setBounds(5, settings.getY() - 60, 60, 60);
        home.addEventListener(this);
        this.add(home);

        logout.setBounds(5, settings.getY() + 65, 60, 60);
        logout.addEventListener(this);
        this.add(logout);

        play.setBounds(684, 364, 74, 74);
        play.addEventListener(this);
        this.add(play);

        ImageIcon head = new ImageIcon(new URL("https://mc-heads.net/avatar/" + Launcher.authInfos.getUsername() + "/126/nohelm.png"));
        JLabel imageLabel = new JLabel(head);
        imageLabel.setSize(126, 126);
        imageLabel.setLocation(658, 42);
        this.add(imageLabel);

        Font nameFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        JLabel name = new JLabel(Launcher.authInfos.getUsername());
        name.setFont(nameFont);
        name.setSize(126, 16);
        name.setLocation(658, 42 + (126));
        name.setForeground(Color.white);
        name.setHorizontalAlignment(JLabel.CENTER);
        this.add(name);

        this.add(jfxPanel);
        jfxPanel.setBounds(127, 50, 468, 390);

        sideBtn.add(home);
        sideBtn.add(settings);
        sideBtn.add(logout);
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

        g.setColor(getRGBA(47, 47, 47, 250));
        g.fillRoundRect(sideBtn.get(0).getX(), sideBtn.get(0).getY(), 60, sideBtn.get(sideBtn.size() - 1).getY() - sideBtn.get(0).getY() + 60, 20, 20);
    }

    public static void startWebView(){
        /*SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Platform.runLater(() -> {
                    webView = new WebView();
                    Scene scene = new Scene(webView);
                    jfxPanel.setScene(scene);
                    webView.getEngine().load("https://jlskyzer-home.netlify.app/news/news.html");
                    //webView.getEngine().reload();
                });
                return null;
            }
        };
        worker.execute();*/
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == play) {
            if(Launcher.authInfos != null){
                Settings.ramSelector.save();
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
            }else{
                final JFrame parent = new JFrame();
                JLabel title = new JLabel("Veuillez vous connecter avant de lancer le jeu !");

                title.setHorizontalAlignment(JLabel.CENTER);
                title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
                parent.add(title);
                parent.pack();
                parent.setTitle("Erreur");
                parent.setLocationRelativeTo(null);
                parent.setVisible(true);
            }
        } else if(swingerEvent.getSource() == settings){
            try {
                instance.setContentPane(new Settings());
                instance.revalidate();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (swingerEvent.getSource() == close) {
            System.exit(0);
        } else if (swingerEvent.getSource() == hide) {
            Frame.getInstance().setExtendedState(JFrame.ICONIFIED);
        } else if (swingerEvent.getSource() == logout) {
            try {
                Launcher.logout();
            } catch (MicrosoftAuthenticationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class MyCustomScrollbarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            // Personnalisez ici les couleurs de la barre de défilement
            thumbColor = hexToColor("22387A");
            // Couleur du "pouce" de la barre
            trackColor = hexToColor("0B2163"); // Couleur de la "piste" de la barre
        }
    }

    public static Color getRGBA(int r, int g, int b, int alpha) {
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

    public static STexturedButton close;

    static {
        try {
            close = new STexturedButton(getBufferedImage("close.png"), getBufferedImage("close_hover.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static STexturedButton hide;

    static {
        try {
            hide = new STexturedButton(getBufferedImage("hide.png"), getBufferedImage("hide_hover.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
