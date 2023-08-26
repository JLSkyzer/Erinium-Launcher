package fr.eriniumgroup.eriniumlauncher.utils;

import fr.eriniumgroup.eriniumlauncher.Launcher;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;

public class MicrosoftThread implements  Runnable{
    @Override
    public void run() {
        try {
            Launcher.auth();
        } catch (MicrosoftAuthenticationException e) {
            Launcher.getReporter().catchError(e, "Impossible de se connecter");
        }
    }
}
