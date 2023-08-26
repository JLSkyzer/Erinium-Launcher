package fr.eriniumgroup.eriniumlauncher;

import javax.imageio.ImageIO;
import javax.swing.*;
import fr.theshark34.swinger.util.WindowMover;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Frame extends JFrame {

    private static Frame instance;
    private Panel panel;
    public Frame() throws IOException {
        instance = this;
        this.setTitle("EriniumLauncher");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 450);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(getImage("icon.png"));
        this.setContentPane(panel = new Panel());

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Launcher.crashFile.mkdirs();

        instance = new Frame();
    }

    public static Image getImage(String fichier) throws IOException{
        InputStream inputStream = Frame.getInstance().getClass().getClassLoader().getResourceAsStream(fichier);
        return ImageIO.read(inputStream);
    }

    public static BufferedImage getBufferedImage(String fichier) throws IOException{
        InputStream inputStream = Frame.getInstance().getClass().getClassLoader().getResourceAsStream(fichier);
        return ImageIO.read(inputStream);
    }

    public static Frame getInstance() {
        return instance;
    }

    public Panel getPanel() {
        return this.panel;
    }
}
