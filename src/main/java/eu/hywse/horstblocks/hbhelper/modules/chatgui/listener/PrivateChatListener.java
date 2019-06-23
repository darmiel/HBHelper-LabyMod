package eu.hywse.horstblocks.hbhelper.modules.chatgui.listener;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.ChatGuiModule;
import eu.hywse.horstblocks.hbhelper.utils.Settings;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.utils.ModColor;

import javax.swing.*;
import java.util.Set;

public class PrivateChatListener implements MessageReceiveEvent {

    @Override
    public boolean onReceive(String s, String s1) {

        String cleanMsg = ModColor.removeColor(s1);
        if (cleanMsg.startsWith("Spieler '") && cleanMsg.endsWith("' ist nicht online!")) {
            String username = cleanMsg.substring(9, cleanMsg.length() - 19);
            if (ChatGuiModule.hasChat(username)) {
                ChatGuiModule.getChat(username).appendComment("Der Spieler ist nicht (mehr) online!");
            }
            return false;
        }

        // Msg
        if (cleanMsg.startsWith("▌ MSG > ")) {
            SwingUtilities.invokeLater(() -> {
                String clean = cleanMsg.substring(8);
                boolean action = false;

                // Empfangen
                if (clean.matches("[A-Za-z0-9_]{1,16}\\s>(.*)")) {
                    String message = clean.substring(clean.indexOf(">") + 1).trim();
                    String username = clean.substring(0, clean.indexOf(">")).trim();

                    // Reopen chat
                    if (ChatGuiModule.getChat(username).isClosed()) {
                        ChatGuiModule.getChat(username).open();
                    }

                    ChatGuiModule.getChat(username).received(message);
                    action = true;
                } else if (clean.matches("[A-Za-z0-9_]{1,16}\\s<(.*)")) {
                    String message = clean.substring(clean.indexOf("<") + 1).trim();
                    String username = clean.substring(0, clean.indexOf("<")).trim();

                    // Reopen chat
                    if (ChatGuiModule.getChat(username).isClosed()) {
                        ChatGuiModule.getChat(username).open();
                    }

                    ChatGuiModule.getChat(username).sent(message);
                    action = true;
                }

                if(action) {
                    if(Settings.msgOpenGuiOnMessage
                        && !HelperAddon.getInstance().getChatGuiModule().isVisible()) {
                        HelperAddon.getInstance().getChatGuiModule().setVisible(true);
                    }
                }
            });
        }

        return false;
    }

}
