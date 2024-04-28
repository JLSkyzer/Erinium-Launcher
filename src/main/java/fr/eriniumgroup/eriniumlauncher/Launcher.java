package fr.eriniumgroup.eriniumlauncher;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.ForgeVersionType;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.swinger.Swinger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import static fr.eriniumgroup.eriniumlauncher.Frame.getImage;
import static fr.eriniumgroup.eriniumlauncher.Frame.instance;

public class Launcher {

    private static GameInfos gameInfos = new GameInfos("Joblife", new GameVersion("1.20.1", GameType.V1_13_HIGHER_FORGE), new GameTweak[]{GameTweak.FORGE});
    private static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(String.valueOf(path), "craches");
    private static CrashReporter reporter = new CrashReporter(String.valueOf(crashFile), crashFile);
    public static AuthInfos authInfos;

    public static void auth() throws MicrosoftAuthenticationException, IOException, AWTException, BadLocationException {
        Connect.info.setForeground(Color.green);
        Connect.info.setText("Connexion en cours...");
        MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
        final String refresh_token = Frame.getSaver().get("refresh_token");
        MicrosoftAuthResult result = null;
        if (refresh_token != null && !refresh_token.isEmpty()){
            result = microsoftAuthenticator.loginWithRefreshToken(refresh_token);
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
        } else{
            result = microsoftAuthenticator.loginWithWebview();
            Frame.getSaver().set("refresh_token", result.getRefreshToken());
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
        }
        instance.setContentPane(new Panel());
        instance.revalidate();
        Panel.resetbarlabel();
        Connect.microsoft.enable();
        Connect.info.setText("");
        displayTray("Joblife launcher", "Bienvenue " + authInfos.getUsername() + " !");
    }

    public static void directauth() throws MicrosoftAuthenticationException, IOException, AWTException, BadLocationException {
        Connect.info.setText("Connexion en cours...");
        MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
        final String refresh_token = Frame.getSaver().get("refresh_token");
        MicrosoftAuthResult result = null;
        if (refresh_token != null && !refresh_token.isEmpty()){
            result = microsoftAuthenticator.loginWithRefreshToken(refresh_token);
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
            // connexion réussi donc connexion automatique
            instance.setContentPane(new Panel());
            instance.revalidate();
            Panel.resetbarlabel();
            displayTray("Joblife launcher", "Bienvenue " + authInfos.getUsername() + " !");
        }else{
            Connect.microsoft.enable();
            Connect.info.setForeground(Color.RED);
            Connect.info.setText("Connexion automatique échoué, veuillez vous connecter en cliquant sur le bouton");
        }
    }

    public static void logout() throws MicrosoftAuthenticationException, IOException, AWTException{
        Frame.getSaver().remove("refresh_token");
        displayTray("Erinium", "Vous avez été déconnecter !");
        instance.setContentPane(new Connect());
        instance.revalidate();
        Connect.microsoft.enable();
        Connect.info.setText("Connectez vous");
        Connect.info.setForeground(Color.green);
    }

    public static void update() throws Exception {

        VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder().withName("1.20.1").build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().build();

        AbstractForgeVersion version = new ForgeVersionBuilder(ForgeVersionType.NEW).withFileDeleter(new ModFileDeleter(false)).withForgeVersion("47.2.20").withMods("https://eriniumadventure.fr/updater.php").build();
        Collection<ExternalFile> externalFile = ExternalFile.getExternalFilesFromJson("https://eriniumadventure.fr/updater.php");

        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVanillaVersion(vanillaVersion).withUpdaterOptions(options).withModLoaderVersion(version).withExternalFiles(externalFile).withProgressCallback(new IProgressCallback() {

            Step etape;
            int currfile;
            int maxfile;
            int val;
            int max;

            @Override
            public void update(DownloadList.DownloadInfo info) {
                IProgressCallback.super.update(info);

                val = (int) (info.getDownloadedBytes() / 1000);
                max = (int) (info.getTotalToDownloadBytes() / 1000);
                currfile = info.getDownloadedFiles();
                maxfile = info.getTotalToDownloadFiles();

                Panel.progressBar.setMaximum(max);
                Panel.progressBar.setValue(val);

                Panel.barLabel.setText("Telechargement des fichiers : " + currfile + " / " + maxfile + " (" + Swinger.percentage(val, max) + "%)");

                Panel.progressBar.revalidate();
                Panel.barLabel.revalidate();
            }
        }).build();

        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    updater.update(path);
                } catch (Exception e) {
                    System.out.println(e);
                }
                Panel.barLabel.setText("Lancement du jeu...");
                try {
                    launch();
                } catch (Exception e) {
                    System.out.println(e);
                }
                super.run();
            }
        };
        t.start();
    }

    public static void launch() throws Exception {
        NoFramework noFramework = new NoFramework(path, authInfos, GameFolder.FLOW_UPDATER);
        noFramework.getAdditionalVmArgs().add("-Xmx" + Settings.readRam() + "G");
        Process p = noFramework.launch("1.20.1", "47.2.20", NoFramework.ModLoader.FORGE);
        instance.setVisible(false);

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("Jeu fermée");
        Panel.play.enable();
        instance.setContentPane(new Panel());
        instance.revalidate();
        Panel.isGameLaunch = false;
        Panel.resetbarlabel();
    }

    public static CrashReporter getReporter() {
        return reporter;
    }

    public static Path getPath() {
        return path;
    }

    public static void displayTray(String title, String message) throws AWTException, IOException {
        if (SystemTray.isSupported()){
            instance.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        }

        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = getImage("img.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Joblife icon");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray");
        //tray.add(trayIcon);

        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);

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
            tray.remove(trayIcon);
            tray.add(trayIcon);
        }catch (AWTException e1){
            e1.printStackTrace();
        }
    }
}
