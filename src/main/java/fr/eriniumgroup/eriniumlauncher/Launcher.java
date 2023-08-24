package fr.eriniumgroup.eriniumlauncher;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;

import java.io.File;
import java.nio.file.Path;

public class Launcher {

    private static GameInfos gameInfos = new GameInfos("EriniumLauncher", new GameVersion("1.16.5", GameType.V1_13_HIGHER_FORGE), new GameTweak[]{GameTweak.FORGE});
    private static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(String.valueOf(path), "craches");
    private static CrashReporter reporter = new CrashReporter(String.valueOf(crashFile), path);
    private static AuthInfos authInfos;

    public static void auth() throws MicrosoftAuthenticationException {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        MicrosoftAuthResult result = authenticator.loginWithWebview();
        authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());

    }
}
