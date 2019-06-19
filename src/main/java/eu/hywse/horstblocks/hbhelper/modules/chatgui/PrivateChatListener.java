package eu.hywse.horstblocks.hbhelper.modules.chatgui;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.utils.ModColor;

public class PrivateChatListener implements MessageReceiveEvent {

    @Override
    public boolean onReceive(String s, String s1) {

        // Only when visible
        if (!HelperAddon.getInstance().getMainChatGui().isVisible()
                && HelperAddon.getInstance().isOnlyLogWhenVisible()) {
            return false;
        }

        MainChatGui gui = HelperAddon.getInstance().getMainChatGui();

        String cleanMsg = ModColor.removeColor(s1);
        if (cleanMsg.startsWith("Spieler '") && cleanMsg.endsWith("' ist nicht online!")) {
            String username = cleanMsg.substring(9, cleanMsg.length() - 19);
            if (gui.hasChat(username)) {
                gui.getChat(username).appendComment("Der Spieler ist nicht (mehr) online!");
            }
            return false;
        }

        // Msg
        if (cleanMsg.startsWith("▌ MSG > ")) {
            HelperAddon.getService().submit(() -> {
                String clean = cleanMsg.substring(8);

                // Empfangen
                if (clean.matches("[A-Za-z0-9_]{1,16}\\s>(.*)")) {
                    String message = clean.substring(clean.indexOf(">") + 1).trim();
                    String username = clean.substring(0, clean.indexOf(">")).trim();

                    gui.getChat(username).received(message);

                    // Reopen chat
                    if (gui.getChat(username).isClosed()) {
                        gui.getChat(username).open();
                    }
                } else if (clean.matches("[A-Za-z0-9_]{1,16}\\s<(.*)")) {
                    String message = clean.substring(clean.indexOf("<") + 1).trim();
                    String username = clean.substring(0, clean.indexOf("<")).trim();

                    gui.getChat(username).sent(message);

                    // Reopen chat
                    if (gui.getChat(username).isClosed()) {
                        gui.getChat(username).open();
                    }
                }
            });
        }

        if (cleanMsg.contains("123abc456def")) {
            gui.setVisible(true);
        }

        return false;
    }

}
