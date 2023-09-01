package fr.eriniumgroup.eriniumlauncher;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.theshark34.openlauncherlib.util.LogUtil;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static fr.eriniumgroup.eriniumlauncher.Frame.*;
import static fr.eriniumgroup.eriniumlauncher.Panel.*;

public class Settings extends JPanel implements SwingerEventListener {
    private Image background = getImage("launcher.png");
    public static RamSelector ramSelector = new RamSelector(Frame.getRamFile());
    private SColoredButton saveRamBtn = new SColoredButton(new Color(11, 33, 99), new Color(34, 56, 122));
    private STexturedButton settings = new STexturedButton(getBufferedImage("settings.png"), getBufferedImage("settings_hover.png"));
    private STexturedButton home = new STexturedButton(getBufferedImage("home.png"), getBufferedImage("home_hover.png"));
    private STexturedButton logout = new STexturedButton(getBufferedImage("logout.png"), getBufferedImage("logout_hover.png"));
    private JSlider ram = new JSlider(JSlider.HORIZONTAL, 1, 32, Integer.parseInt(readRam()));
    private List<Component> sideBtn = new ArrayList<>();

    public Settings() throws IOException {
        this.setLayout(null);

        close.setBounds(Frame.getInstance().getWidth() - 45, 0, 40, 40);
        close.addEventListener(this);
        this.add(close);

        hide.setBounds(close.getX() - 45, 20 - (17 / 2), 40, 17);
        hide.addEventListener(this);
        this.add(hide);

        Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
        JLabel title = new JLabel("Séléctionnez votre ram");
        title.setFont(titleFont);
        title.setSize(497, titleFont.getSize());
        title.setLocation(117, 44);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);
        this.add(title);

        ram.setLocation(117 + ((497 / 2) - 150), 44 + 201 - 10);
        ram.setSize(300, 25);
        //g.fillRoundRect(117, 44, 497, 402, 20, 20);

        JLabel label = new JLabel(ram.getValue() + " GB");
        label.setLocation(ram.getX(), ram.getY() - 18);
        label.setSize(300, 18);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(JLabel.CENTER);

        saveRamBtn.setBounds(ram.getX() + 75, ram.getY() + 50, 150, 30);
        saveRamBtn.setForeground(Color.white);
        saveRamBtn.addEventListener(this);

        Font saveRamBtnFont = new Font(Font.SANS_SERIF, Font.BOLD, 24);
        JLabel saveRam = new JLabel("Sauvegarder la ram");
        saveRam.setFont(saveRamBtnFont);
        saveRam.setBounds(saveRamBtn.getX(), saveRamBtn.getY(), 150, 30);
        saveRam.setForeground(Color.white);
        saveRam.setHorizontalAlignment(JLabel.CENTER);
        this.add(saveRam);

        ram.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Mettre à jour le texte du label avec la nouvelle valeur du slider
                label.setText(ram.getValue() + " GB");
            }
        });

        this.add(ram);
        this.add(label);
        this.add(saveRamBtn);

        settings.setBounds(5, Frame.getInstance().getHeight() / 2 - 30,60,60);
        settings.setLocation(5, Frame.getInstance().getHeight() / 2 /*((sideBtn.size() * 30) / 2)*/);
        settings.addEventListener(this);
        this.add(settings);

        home.setBounds(5, settings.getY() - 60, 60, 60);
        home.addEventListener(this);
        this.add(home);

        logout.setBounds(5, settings.getY() + 65, 60, 60);
        logout.addEventListener(this);
        this.add(logout);

        sideBtn.add(home);
        sideBtn.add(settings);
        sideBtn.add(logout);
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

        g.setColor(getRGBA(47, 47, 47, 250));
        g.fillRoundRect(sideBtn.get(0).getX(), sideBtn.get(0).getY(), 60, (sideBtn.size()) * 60, 20, 20);
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if(swingerEvent.getSource() == saveRamBtn){
            saveRam();
            try {
                Launcher.displayTray("Erinium Launcher", "Ram sauvegarder !");
            } catch (AWTException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if(swingerEvent.getSource() == home) {
            try {
                instance.setContentPane(new Panel());
                instance.revalidate();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        } else if (swingerEvent.getSource() == close) {
            System.exit(0);
        } else if (swingerEvent.getSource() == hide) {
            Frame.getInstance().setExtendedState(JFrame.ICONIFIED);
        } else if (swingerEvent.getSource() == logout) {
            try {
                Launcher.logout();
            } catch (MicrosoftAuthenticationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static RamSelector getRamSelector() {
        return ramSelector;
    }

    public static String readRam(){
        try(BufferedReader br = Files.newBufferedReader(getRamFile().toPath(), StandardCharsets.UTF_8))
        {
            final String ramText = br.readLine();

            if (ramText != null) {
                return ramText;
            } else{
                LogUtil.err("warn", "ram-empty");
            }
        } catch (IOException e) {
            System.err.println("[OpenLauncherLib] WARNING: Can't read ram : " + e);
        }
        return "0";
    }

    public void saveRam(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(getRamFile(), StandardCharsets.UTF_8))) {
            bw.write(String.valueOf(ram.getValue()));
        } catch (IOException e) {
            System.err.println("[OpenLauncherLib] WARNING: Can't save ram : " + e);
        }
    }
}


/*public void save()
{
    if (this.frame == null) return;

    try(BufferedWriter bw = Files.newBufferedWriter(this.file, StandardCharsets.UTF_8))
    {
        bw.write(String.valueOf(this.frame.getSelectedIndex()));
    } catch (IOException e)
    {
        System.err.println("[OpenLauncherLib] WARNING: Can't save ram : " + e);
    }
}
*/