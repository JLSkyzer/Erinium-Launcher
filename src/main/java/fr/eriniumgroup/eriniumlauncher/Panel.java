package fr.eriniumgroup.eriniumlauncher;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatLineBorder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.eriniumgroup.eriniumlauncher.utils.GetJsonObjetUrl;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static fr.eriniumgroup.eriniumlauncher.Frame.*;

public class Panel extends JPanel implements SwingerEventListener {

    private Image background = getImage("launcher.png");
    public static STexturedButton play;
    private STexturedButton settings = new STexturedButton(getBufferedImage("settings.png"), getBufferedImage("settings_hover.png"));
    private STexturedButton home = new STexturedButton(getBufferedImage("home.png"), getBufferedImage("home_hover.png"));
    private STexturedButton logout = new STexturedButton(getBufferedImage("logout.png"), getBufferedImage("logout_hover.png"));
    private List<Component> sideBtn = new ArrayList<>();
    private JScrollPane scrollPane = new JScrollPane();
    public static JLabel updaterInfo = new JLabel();
    public static SColoredBar progressBar = new SColoredBar(new Color(255, 255, 255), hexToColor("0B2163"));
    public static JLabel barLabel = new JLabel();
    public static boolean isGameLaunch = false;

    public Panel() throws IOException, BadLocationException {
        this.setLayout(null);
        FlatLightLaf.setup();

        updaterInfo.setLocation(40, 20);
        updaterInfo.setForeground(Color.RED);
        this.add(updaterInfo);

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

        JPanel newspanel = new JPanel(new GridLayout());
        newspanel.setLayout(new BoxLayout(newspanel, BoxLayout.Y_AXIS));
        newspanel.setSize(450, 360);
        newspanel.setBackground(hexToColor("22387A"));

        //setBounds(137, 50, 460, 390)
        scrollPane.setBounds(127, 50, 480, 360);
        scrollPane.setViewportView(newspanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setMinimumSize(new Dimension(480, 360));
        scrollPane.setPreferredSize(new Dimension(480, 360));
        scrollPane.setFocusable(true);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUI(new MyCustomScrollbarUI(hexToColor("0B2163"), hexToColor("22387A")));
        scrollPane.setBorder(null);

        sideBtn.add(home);
        sideBtn.add(settings);
        sideBtn.add(logout);

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(GetJsonObjetUrl.getNews().toString()).getAsJsonArray();

        for (int i = jsonArray.size() - 1; i >= 0; i--) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            String date = jsonObject.get("date").getAsString();
            String autor = jsonObject.get("name").getAsString();
            String title = jsonObject.get("title").getAsString();
            String message = jsonObject.get("message").getAsString();

            /*doc.insertString(doc.getLength(), title, null);*/
            JLabel titlemessage = new JLabel("<html><p style=\"width:350px\"" + "align='center'>" + title + "</p></html>");
            titlemessage.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
            titlemessage.setAlignmentX(Component.CENTER_ALIGNMENT);
            titlemessage.setHorizontalAlignment(JLabel.CENTER);

            JLabel infomessage = new JLabel("<html><p style=\"width:350px\"" + "align='center'>" + "par " + autor + " le " + date + "</p></html>");
            infomessage.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            /*infomessage.setSize(newspanel.getWidth(), infomessage.getPreferredSize().height);*/
            infomessage.setHorizontalAlignment(JLabel.CENTER);
            infomessage.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel getmessage = new JLabel("<html><p style=\"width:350px\"" + "align='center'>" + message + "</p></html>");
            getmessage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            //getmessage.setSize(newspanel.getWidth(), getmessage.getPreferredSize().height);
            getmessage.setHorizontalAlignment(JLabel.CENTER);
            getmessage.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel separator = new JPanel();
            separator.setBackground(Color.WHITE);
            separator.setSize(new Dimension(newspanel.getWidth(), 5));
            separator.setMaximumSize(new Dimension(newspanel.getWidth(), 5));
            separator.setBorder( new FlatLineBorder( new Insets( 0, 0, 0, 0 ), Color.white, 2, 10));

            titlemessage.setForeground(Color.white);
            infomessage.setForeground(Color.white);
            getmessage.setForeground(Color.white);

            JPanel tempPanel = new JPanel(new GridLayout());
            tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));

            newspanel.add(titlemessage);
            newspanel.add(infomessage);
            newspanel.add(Box.createRigidArea(new Dimension(0, 5)));
            newspanel.add(getmessage);
            newspanel.add(Box.createRigidArea(new Dimension(0, 5)));
            newspanel.add(separator);
            newspanel.add(Box.createRigidArea(new Dimension(0, 5)));

            scrollPane.revalidate();
        }
        this.add(scrollPane);

        progressBar.setBounds(scrollPane.getX(), scrollPane.getY() + scrollPane.getHeight() + 25, scrollPane.getWidth(), 10);
        progressBar.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        progressBar.setValue(0);
        this.add(progressBar);

        barLabel.setBounds(progressBar.getX(), progressBar.getY() - 22, progressBar.getWidth(), 20);
        barLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        //barLabel.setText("Clique sur jouer !");
        barLabel.setForeground(Color.white);
        barLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(barLabel);
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
        g.fillRoundRect(sideBtn.get(0).getX(), sideBtn.get(0).getY(), 60, sideBtn.get(sideBtn.size() - 1).getY() - sideBtn.get(0).getY() + 62, 20, 20);
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == play) {
            play.disable();
            if(Launcher.authInfos != null){
                Settings.ramSelector.save();
                if(!isGameLaunch){
                    isGameLaunch = true;
                    try {
                        Launcher.update();
                    } catch (Exception e) {
                        Launcher.getReporter().catchError(e, "Impossible de mettre à jour le launcher");
                    }
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

    public static void resetbarlabel(){
        barLabel.setText("Clique sur jouer !");
    }

    class MyCustomScrollbarUI extends BasicScrollBarUI {
        private Color thumbColor;
        private Color trackColor;

        public MyCustomScrollbarUI(Color thumbColor, Color trackColor) {
            this.thumbColor = thumbColor;
            this.trackColor = trackColor;
        }

        @Override
        protected void configureScrollBarColors() {
            // Définissez les couleurs personnalisées pour le pouce (thumb) et la piste (track)
            thumbColor = thumbColor != null ? thumbColor : super.thumbColor;
            trackColor = trackColor != null ? trackColor : super.trackColor;

            super.thumbColor = thumbColor;
            super.thumbHighlightColor = thumbColor.brighter();
            super.thumbDarkShadowColor = thumbColor.darker();
            super.thumbLightShadowColor = thumbColor.brighter().brighter();
            super.trackColor = trackColor;
            super.trackHighlightColor = trackColor;
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

    public SColoredBar getProgressBar(){
        return progressBar;
    }

    public static void setBarLabelText(String text){
        barLabel.setText(text);
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

    static {
        try {
            play = new STexturedButton(getBufferedImage("play.png"), getBufferedImage("play_hover.png"));
            play.setTextureDisabled(getImage("play_disable.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
