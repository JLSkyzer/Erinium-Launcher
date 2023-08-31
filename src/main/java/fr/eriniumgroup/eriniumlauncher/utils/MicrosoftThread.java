package fr.eriniumgroup.eriniumlauncher.utils;

import fr.eriniumgroup.eriniumlauncher.Launcher;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;

import java.awt.*;
import java.io.IOException;

public class MicrosoftThread implements  Runnable{
    @Override
    public void run() {
        try {
            Launcher.auth();
        } catch (MicrosoftAuthenticationException | IOException e) {
            Launcher.getReporter().catchError(e, "Impossible de se connecter");
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}
