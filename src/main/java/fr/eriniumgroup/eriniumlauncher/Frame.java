package fr.eriniumgroup.eriniumlauncher;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.util.WindowMover;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Frame extends JFrame {

    public static Frame instance;
    public Panel panel;
    private static File ramFile = new File(String.valueOf(Launcher.getPath()), "ram.txt");
    private static File saverFile = new File(String.valueOf(Launcher.getPath()), "user.stock");
    private static Saver saver = new Saver(saverFile);
    public Frame() throws IOException {
        instance = this;
        this.setTitle("EriniumLauncher");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 450);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(getImage("icon.png"));
        this.setContentPane(new Connect());

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException, MicrosoftAuthenticationException, AWTException, BadLocationException {
        Launcher.crashFile.mkdirs();
        if(!ramFile.exists()){
            ramFile.createNewFile();
        }
        if (!saverFile.exists()){
            saverFile.createNewFile();
        }

        instance = new Frame();
        Launcher.directauth();
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

    public static File getRamFile() {
        return ramFile;
    }

    public static Saver getSaver() {
        return saver;
    }
}
