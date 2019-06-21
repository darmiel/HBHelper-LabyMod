package eu.hywse.horstblocks.hbhelper.modules.chatgui.gui;


import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.ChatGuiModule;
import eu.hywse.horstblocks.hbhelper.utils.HastebinAPI;
import eu.hywse.horstblocks.hbhelper.utils.PlayerHead;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserChat extends UserChatDesigner {

    @Getter
    private int unreadMessages = 0;

    @Getter
    private boolean closed;

    public UserChat(String username, JTabbedPane tabbedPane) {
        super(username, tabbedPane);
        registerListener();
    }

    private void addTab(JTabbedPane tabbedPane) {
        // Prüfe ob der Tab vielleicht schon offen ist
        for (Component component : tabbedPane.getComponents()) {
            if (SwingUtilities.isDescendingFrom(component, this)) {
                return;
            }
        }
        tabbedPane.addTab(getUsername(), this);

        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        pnlTab.add(new JLabel(tabbedPane.getTitleAt(getTabIndex())), gbc);
        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(new JButton("\uD83D\uDD12"), gbc);
        tabbedPane.setTabComponentAt(getTabIndex(), pnlTab);

        // Async icon load
        HelperAddon.getService().execute(() -> {
            int index = getTabIndex();
            if (index == -1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
            index = getTabIndex();
            if (index == -1) {
                return;
            }

            tabbedPane.setIconAt(index, PlayerHead.getIcon(getUsername(), 32));
        });
    }

    private void registerListener() {
        // Close Button
        btnClose.addActionListener(e -> close());

        // Send  Button
        btnSend.addActionListener(e -> send());
        txtMsg.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    e.consume();
                    send();
                }
            }
        });

        // Clear chat
        btnClearChat.addActionListener(e -> {
            txtChat.setText("");
            appendComment("Chat geleert!");
        });

        // Updateload
        btnUploadHastebin.addActionListener(e -> {
            String content = txtChat.getText();
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
        btnCloseRead.addActionListener(e -> {
            for (UserChat chat : ChatGuiModule.getChats().values()) {
                if (chat.getUnreadMessages() == 0) {
                    chat.close();
                }
            }
        });

        btnDeleteChat.addActionListener(e -> {
            close();
            ChatGuiModule.getChats().values().removeIf(chat -> chat == this);
        });
    }

    public void open() {
        closed = false;
        addTab(tabbedPane);

        HelperAddon.getInstance().getChatGuiModule().pack();
        appendComment("\uD83D\uDC4B Chat geöffnet am " + new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm:ss.SS").format(new Date()));
    }

    public void close() {
        closed = true;
        tabbedPane.remove(this);

        // Bei neuer Nachricht wieder öffnen
        appendComment("\uD83D\uDD12 Chat geschlossen am " + new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm:ss.SS").format(new Date()));
    }

    public void updateTitle() {
        int index = getTabIndex();
        if (index == -1) {
            return;
        }

        if (inFocus()) {
            unreadMessages = 0;
        }

        tabbedPane.setTitleAt(index, getUsername() + (unreadMessages > 0 ? " (" + unreadMessages + ")" : ""));
    }


    /*
     * Methods
     */
    public void received(String message) {
        addChatMessage(getUsername(), message);

        if (!inFocus()) {
            unreadMessages++;

            // TODO: Ton ändern
            SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.experience_orb.pickup"));
            if (event == null) {
                System.out.println("WARNING! Sound not found!");
            } else {
                Minecraft.getMinecraft().player.playSound(event, 1F, 1F);
            }
        }

        updateTitle();
    }

    public void sent(String message) {
        addChatMessage(null, message);
    }

    private void send() {
        send(null);
    }

    private void send(String msg) {
        String message = (msg == null ? txtMsg.getText().trim() : msg);

        // Min length
        if (message.length() == 0) {
            return;
        }

        // Max length
        if (message.length() > 128) {
            message = message.substring(0, 128);
        }

        // Nachricht senden
        Minecraft.getMinecraft().player.sendChatMessage("/msg " + getUsername() + " " + message);

        // Fokus auf Chatfeld
        if (msg == null) {
            txtMsg.grabFocus();
            txtMsg.setText("");
        }

        // Update unread
        updateTitle();
    }
}
