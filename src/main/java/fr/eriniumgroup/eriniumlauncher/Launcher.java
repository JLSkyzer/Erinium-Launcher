package fr.eriniumgroup.eriniumlauncher;

import com.google.gson.stream.JsonReader;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.utils.builderapi.BuilderArgument;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.configuration.api.json.JSONReader;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Launcher {

    private static GameInfos gameInfos = new GameInfos("EriniumLauncher", new GameVersion("1.16.5", GameType.V1_13_HIGHER_FORGE), new GameTweak[]{GameTweak.FORGE});
    private static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(String.valueOf(path), "craches");
    private static CrashReporter reporter = new CrashReporter(String.valueOf(crashFile), crashFile);
    private static AuthInfos authInfos;

    public static void auth() throws MicrosoftAuthenticationException {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        MicrosoftAuthResult result = authenticator.loginWithWebview();
        authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
    }

    public static void update() throws Exception, IOException {

        VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder().withName("1.16.5").build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().build();

        AbstractForgeVersion version = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW).withFileDeleter(new ModFileDeleter(true)).withForgeVersion("36.2.39").withMods("https://erinium.000webhostapp.com/updater.php").build();
        Collection<ExternalFile> externalFile = ExternalFile.getExternalFilesFromJson("https://erinium.000webhostapp.com/updater.php");

        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVanillaVersion(vanillaVersion).withUpdaterOptions(options).withModLoaderVersion(version).withExternalFiles(externalFile).build();
        updater.update(path);
    }

    public static void crack(){
        authInfos = new AuthInfos("JLSkyzer", "464646495", "qfq4f6q4f6q4f");
    }
    public static void launch() throws Exception {
        NoFramework noFramework = new NoFramework(path, authInfos, GameFolder.FLOW_UPDATER);
        noFramework.launch("1.16.5", "36.2.39", NoFramework.ModLoader.FORGE);
    }

    public static CrashReporter getReporter() {
        return reporter;
    }
}
