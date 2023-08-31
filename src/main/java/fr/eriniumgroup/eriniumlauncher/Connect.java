package fr.eriniumgroup.eriniumlauncher;

import fr.eriniumgroup.eriniumlauncher.utils.MicrosoftThread;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static fr.eriniumgroup.eriniumlauncher.Frame.*;
import static fr.eriniumgroup.eriniumlauncher.Panel.*;

public class Connect extends JPanel implements SwingerEventListener {
    private Image background = getImage("launcher.png");
    public static STexturedButton microsoft;
    static {
        try {
            microsoft = new STexturedButton(getBufferedImage("microsoft.png"), getBufferedImage("microsoft_hover.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JLabel info = new JLabel("");
    private Color color;

    public Connect() throws IOException {
        this.setLayout(null);

        close.setBounds(Frame.getInstance().getWidth() - 45, 0, 40, 40);
        close.addEventListener(this);
        this.add(close);

        hide.setBounds(close.getX() - 45, 20 - (17 / 2), 40, 17);
        hide.addEventListener(this);
        this.add(hide);

        microsoft.setBounds((117 + 497 / 2) - 50, (44 + 201) - 50, 100, 100);
        microsoft.addEventListener(this);
        this.add(microsoft);

        Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
        JLabel title = new JLabel("Connectez vous");
        title.setFont(titleFont);
        title.setSize(497, titleFont.getSize());
        title.setLocation(117, 44);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);

        Font infoFont = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        info.setForeground(Color.green);
        info.setFont(infoFont);
        info.setSize(400, infoFont.getSize());
        info.setLocation(117 + (97 / 2), microsoft.getY() + 88);
        info.setHorizontalAlignment(JLabel.CENTER);

        this.add(title);
        this.add(info);
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

        g.setColor(getRGBA(47, 47, 47, 200));
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == microsoft){
            Thread t = new Thread(new MicrosoftThread());
            t.start();
        } else if (swingerEvent.getSource() == close) {
            System.exit(0);
        } else if (swingerEvent.getSource() == hide) {
            Frame.getInstance().setExtendedState(JFrame.ICONIFIED);
        }
    }
}
