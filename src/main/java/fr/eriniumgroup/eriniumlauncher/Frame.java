package fr.eriniumgroup.eriniumlauncher;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.util.WindowMover;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class Frame extends JFrame {

    public static Frame instance;
    public Panel panel;
    private static File ramFile = new File(String.valueOf(Launcher.getPath()), "ram.txt");
    private static File saverFile = new File(String.valueOf(Launcher.getPath()), "user.stock");
    private static Saver saver = new Saver(saverFile);
    public static boolean tryLunched = false;
    public Frame() throws IOException, AWTException {
        instance = this;
        this.setTitle("JoblifeLauncher");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 450);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(getImage("img.png"));
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
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(ramFile));
            bufferedWriter.write("1");
            bufferedWriter.close();
        }
        if (!saverFile.exists()){
            saverFile.createNewFile();
        }

        instance = new Frame();
        Launcher.directauth();

        launchRPC();
        launchTraySystem();
    }

    public static void launchRPC(){
        final DiscordRPC lib = DiscordRPC.INSTANCE;
        final String appID = "929797081307181078";
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(appID, handlers, true, "");
        DiscordRichPresence discordRichPresence = new DiscordRichPresence();
        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000;
        discordRichPresence.details = "Joue à Joblife Launcher";
        discordRichPresence.state = Launcher.authInfos.getUsername();
        discordRichPresence.largeImageKey = "joblife";

        lib.Discord_UpdatePresence(discordRichPresence);
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

    public static void launchTraySystem() throws IOException {
        if (!tryLunched){
            /*SystemTray systemTray = SystemTray.getSystemTray();
            TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getImage("img.png").toString()));
            PopupMenu popupMenu = new PopupMenu();

            MenuItem show = new MenuItem("Show");
            show.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    instance.setVisible(true);
                }
            });
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            popupMenu.add(show);
            popupMenu.add(exit);

            trayIcon.setPopupMenu(popupMenu);
            try{
                systemTray.remove(trayIcon);
                systemTray.add(trayIcon);
            }catch (AWTException e1){
                e1.printStackTrace();
            }

            tryLunched = true;*/
        }
    }
}
