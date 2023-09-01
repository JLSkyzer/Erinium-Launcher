package fr.eriniumgroup.eriniumlauncher;

import com.sun.jdi.event.StepEvent;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.swinger.Swinger;
import javafx.scene.layout.Pane;

import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EventListener;

import static fr.eriniumgroup.eriniumlauncher.Frame.getImage;
import static fr.eriniumgroup.eriniumlauncher.Frame.instance;

public class Launcher {

    private static GameInfos gameInfos = new GameInfos("EriniumLauncher", new GameVersion("1.16.5", GameType.V1_13_HIGHER_FORGE), new GameTweak[]{GameTweak.FORGE});
    private static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(String.valueOf(path), "craches");
    private static CrashReporter reporter = new CrashReporter(String.valueOf(crashFile), crashFile);
    public static AuthInfos authInfos;

    public static void auth() throws MicrosoftAuthenticationException, IOException, AWTException, BadLocationException {
        Connect.microsoft.disable();
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
        Connect.microsoft.enable();
        Connect.info.setText("");
        displayTray("Erinium Launcher", "Bienvenue " + authInfos.getUsername() + " !");
    }

    public static void directauth() throws MicrosoftAuthenticationException, IOException, AWTException, BadLocationException {
        Connect.microsoft.disable();
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
            displayTray("Erinium Launcher", "Bienvenue " + authInfos.getUsername() + " !");
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

    public static void update() throws Exception, IOException {

        VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder().withName("1.16.5").build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().build();

        AbstractForgeVersion version = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW).withFileDeleter(new ModFileDeleter(true)).withForgeVersion("36.2.39").withMods("https://erinium.000webhostapp.com/updater.php").build();
        Collection<ExternalFile> externalFile = ExternalFile.getExternalFilesFromJson("https://erinium.000webhostapp.com/updater.php");

        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVanillaVersion(vanillaVersion).withUpdaterOptions(options).withModLoaderVersion(version).withExternalFiles(externalFile).build();
        Thread t = new Thread(){
            private int val;
            private int max;
            @Override
            public void run(){
                while (!this.isInterrupted()){
                    val = (int) (updater.getDownloadList().getDownloadInfo().getDownloadedBytes() / 1000);
                    max = (int) (updater.getDownloadList().getDownloadInfo().getTotalToDownloadBytes() / 1000);

                    Panel.progressBar.setMaximum(max);
                    Panel.progressBar.setValue(val);

                    Panel.barLabel.setText("Telechargement des fichier : " + updater.getDownloadList().getDownloadInfo().getDownloadedFiles() + " / " + updater.getDownloadList().getDownloadInfo().getTotalToDownloadFiles());
                }
            }
        };
        t.start();
        updater.update(path);
    }

    public static void crack(){
        authInfos = new AuthInfos("JLSkyzer", "464646495", "qfq4f6q4f6q4f");
    }
    public static void launch() throws Exception {
        NoFramework noFramework = new NoFramework(path, authInfos, GameFolder.FLOW_UPDATER);
        noFramework.getAdditionalVmArgs().add("-Xmx" + Settings.readRam() + "G");
        noFramework.launch("1.16.5", "36.2.39", NoFramework.ModLoader.FORGE);
    }

    public static CrashReporter getReporter() {
        return reporter;
    }

    public static Path getPath() {
        return path;
    }

    public static void displayTray(String title, String message) throws AWTException, IOException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = getImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Erinium icon");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray");
        tray.add(trayIcon);

        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    }
}
