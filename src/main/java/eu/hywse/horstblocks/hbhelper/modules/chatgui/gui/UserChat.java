package eu.hywse.horstblocks.hbhelper.modules.chatgui.gui;


import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.ChatGuiModule;
import eu.hywse.horstblocks.hbhelper.utils.HastebinAPI;
import eu.hywse.horstblocks.hbhelper.utils.PlayerHead;
import eu.hywse.horstblocks.hbhelper.utils.Settings;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserChat extends UserChatDesigner {

    private static final String LBL_CHARS_TEXT = "(%current%/%max% Chars)";

    @Getter
    private int unreadMessages = 0;

    @Getter
    private boolean closed;

    @Getter
    private boolean locked;

    private JPanel pnlTabPanel;
    private JLabel lblTabTitle;

    public UserChat(String username, JTabbedPane tabbedPane) {
        super(username, tabbedPane);
        registerListener();
    }

    private void addTab(JTabbedPane tabbedPane) {
        // Prüfe ob der Tab vielleicht schon offen ist
        for (Component component : tabbedPane.getComponents()) {
            if (!component.isVisible()) {
                continue;
            }
            if (SwingUtilities.isDescendingFrom(component, this)) {
                return;
            }
        }
        tabbedPane.addTab(getUsername(), this);

        // Async icon load
        SwingUtilities.invokeLater(() -> {
            int index = getTabIndex();
            if (index == -1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {

                }
            }
            index = getTabIndex();
            if (index == -1) {
                return;
            }

            /* Tab Panel */

            pnlTabPanel = new JPanel(new GridBagLayout());
            pnlTabPanel.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 2;
            lblTabTitle = new JLabel(tabbedPane.getTitleAt(getTabIndex()),
                    PlayerHead.getIcon(getUsername(), 24),
                    JLabel.LEFT);
            pnlTabPanel.add(lblTabTitle, gbc);

            gbc.gridx++;
            gbc.weightx = 1;
            pnlTabPanel.add(new JLabel(" "));


            gbc.gridx++;
            gbc.weightx = 1;
            JButton btnLock = new JButton("✔");
            btnLock.addActionListener(e -> {
                if (btnLock.getText().equalsIgnoreCase("✔")) {
                    btnLock.setText("LOCKED");
                    UserChat.this.locked = true;
                    lblTabTitle.setForeground(Color.BLUE);
                    lblTabTitle.setFont(lblTabTitle.getFont().deriveFont(lblTabTitle.getFont().getStyle() | Font.BOLD));
                } else {
                    btnLock.setText("✔");
                    UserChat.this.locked = false;
                    lblTabTitle.setForeground(Color.BLACK);
                    lblTabTitle.setFont(lblTabTitle.getFont().deriveFont(lblTabTitle.getFont().getStyle() & ~Font.BOLD));
                }
            });
            pnlTabPanel.add(btnLock);

            gbc.gridx++;
            gbc.weightx = 0;
            JButton btnScndClose = new JButton("X");
            btnScndClose.addActionListener(e -> close());
            pnlTabPanel.add(btnScndClose, gbc);

            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    if (e.getButton() == MouseEvent.BUTTON2) {
                        close();
                    }

                    if (e.getButton() == MouseEvent.BUTTON1) {
                        tabbedPane.setSelectedComponent(UserChat.this);
                    }
                }
            };

            pnlTabPanel.addMouseListener(mouseAdapter);
            lblTabTitle.addMouseListener(mouseAdapter);

            tabbedPane.setTabComponentAt(getTabIndex(), pnlTabPanel);
        });

        updateTitle();
    }

    private int getMaxLength() {
        return 256 - ("/msg " + getUsername() + " ").length();
    }

    private void updateChars() {
        updateChars(false);
    }

    private void updateChars(boolean add) {
        int current = txtInputField.getText().length() + (add ? 1 : 0);
        int max = getMaxLength();

        lblChars.setText(
                LBL_CHARS_TEXT.replace("%current%", String.valueOf(current))
                        .replace("%max%", String.valueOf(max))
        );
    }

    private void registerListener() {
        // Close Button
        btnCloseSingle.addActionListener(e -> close());

        // Send  Button
        btnSend.addActionListener(e -> send());
        txtInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    e.consume();
                    send();
                } else {
                    updateChars(true);
                }
            }
        });

        // Clear chat
        btnClearChat.addActionListener(e -> {
            txtChatBox.setText("");
            appendComment("Chat geleert!");
        });

        // Updateload
        btnUploadHastebin.addActionListener(e -> {
            String content = txtChatBox.getText();
            StringBuilder result = new StringBuilder();

            for (String line : content.split("\\n")) {
                line = line.trim();

                // Ignore comments
                if (line.contains("/*") && line.contains("*/")) {
                    line = line.replaceAll("/\\*(.*)\\*/", "");
                }

                if (line.length() <= 0
                        || line.startsWith("#")
                        || line.startsWith("//")) {
                    continue;
                }

                while (line.contains("]:  ")) {
                    line = line.replace("]:  ", "]: ");
                }

                result.append(result.length() > 0 ? "\n" : "")
                        .append(line);
            }

            if (result.length() <= 0) {
                appendComment("Es gibt nichts zum Hochladen!");
                return;
            }

            appendComment("Hastebin-URL: " + HastebinAPI.upload(result.toString(), true, false) + " [in die Zwischenablage kopiert]");
        });

        // Close
        btnCloseAll.addActionListener(e -> {
            for (UserChat chat : ChatGuiModule.getChats().values()) {
                if (chat.getUnreadMessages() == 0
                        && !chat.isLocked()) {
                    chat.close();
                }
            }
        });

        btnDelete.addActionListener(e -> {
            close(true);
            ChatGuiModule.getChats().values().removeIf(chat -> chat == this);
        });
    }

    /**
     * Öffnet eine Konversation mit einem user
     */
    public void open() {
        closed = false;

        addTab(tabbedPane);

        if (HelperAddon.getInstance().getChatGuiModule().getTabbedPane().getTabCount() == 1) {
            HelperAddon.getInstance().getChatGuiModule().pack();
        }

        appendComment("\uD83D\uDC4B Chat geöffnet am " + new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm:ss.SS").format(new Date()));
    }


    /**
     * Schließt eine Konversation mit einem Nutzer (FORCED)
     */
    public void close() {
        close(false);
    }

    /**
     * Schließt eine Konversation mit einem Nutzer
     *
     * @param force Ignoriere LOCK-Einstellung
     */
    public void close(boolean force) {

        // Check for locked
        if (isLocked() && !force) {
            JOptionPane.showMessageDialog(this,
                    "Der Chat wurde gelockt\n- daher kann er nicht geschlossen werden!",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        if (force) {
            locked = false;
        }

        closed = true;
        tabbedPane.remove(this);

        // Bei neuer Nachricht wieder öffnen
        appendComment("\uD83D\uDD12 Chat geschlossen am " + new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm:ss.SS").format(new Date()));

        System.out.println("Closed Chat with " + getUsername());
    }

    /**
     * Aktualisiert den Tab-Title
     * Prüft nach ungelesenen Nachrichten
     */
    public void updateTitle() {
        if (inFocus()) {
            unreadMessages = 0;
        }

        String text = getUsername() + (unreadMessages > 0 ? " (" + unreadMessages + ")" : "");

        if (lblTabTitle != null) {
            lblTabTitle.setText(" " + text);
        }

        if (getTabIndex() != -1) {
            tabbedPane.setTitleAt(getTabIndex(), text);
        }
    }

    /**
     * Zeigt eine empfangene Nachricht im Chatfenster an
     *
     * @param message Empfangene Nachricht
     */
    public void received(String message) {
        addChatMessage(getUsername(), message);

        if (!inFocus()) {
            unreadMessages++;

            if (Settings.msgPlaySoundOnMessage) {
                SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(Settings.msgSoundFileName));
                if (event == null) {
                    System.out.println("WARNING! Sound not found!");
                } else {
                    Minecraft.getMinecraft().player.playSound(event, 1F, 1F);
                }
            }
        }

        updateTitle();
    }

    /**
     * Zeigt eine gesendete Nachricht im Chatfenster an
     *
     * @param message Gesendete Nachricht
     */
    public void sent(String message) {
        addChatMessage(null, message);
    }

    /**
     * Sendet die Nachricht, die in das Textfeld eingegeben wurde.
     * Anschließend wird die Eingabetextbox ausgewählt
     */
    private void send() {
        String message = /*(msg == null ? */ txtInputField.getText().trim() /* : msg) */;

        // Min length
        if (message.length() == 0) {
            return;
        }

        // Max length
        if (message.length() > 128) {
            message = message.substring(0, 128);
        }

        message = message.replace("\u00A7", "&");

        // Nachricht senden
        Minecraft.getMinecraft().player.sendChatMessage("/msg " + getUsername() + " " + message);

        // Fokus auf Chatfeld
//        if (msg == null) {
        txtInputField.grabFocus();
        txtInputField.setText("");
//        }

        // Update unread
        updateTitle();
    }
}