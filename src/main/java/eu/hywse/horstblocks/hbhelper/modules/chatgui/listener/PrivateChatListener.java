package eu.hywse.horstblocks.hbhelper.modules.chatgui.listener;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.ChatGuiModule;
import eu.hywse.horstblocks.hbhelper.utils.Settings;
import javax.swing.SwingUtilities;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.client.chat.MessageReceiveEvent;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;

public class PrivateChatListener {

  @Subscribe
  public void onReceive(final MessageReceiveEvent event) {
    System.out.println("onReceive: " + event.toString());

    final String cleanMsg = event.getComponent().getUnformattedComponentText();
    if (cleanMsg.startsWith("Spieler '") && cleanMsg.endsWith("' ist nicht online!")) {
      String username = cleanMsg.substring(9, cleanMsg.length() - 19);
      if (ChatGuiModule.hasChat(username)) {
        ChatGuiModule.getChat(username).appendComment("Der Spieler ist nicht (mehr) online!");
      }
      return;
    }

    // Msg
    if (cleanMsg.startsWith("▌ MSG > ")) {
      SwingUtilities.invokeLater(() -> {
        String clean = cleanMsg.substring(8);
        clean = clean.replace(" ✔ ", " ");

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

          // Auto answer?
          if (Settings.msgAutoAnswerEnabled && Settings.msgAutoAnswerText.length() > 0) {

            // Check for focus
            if (Settings.msgAutoAnswerNotIfInFocus
                //&& HelperAddon.getInstance().getChatGuiModule().isVisible()
                //&& HelperAddon.getInstance().getChatGuiModule().hasFocus()
                && HelperAddon.getInstance().getChatGuiModule().getTabbedPane().getTabCount() > 0) {

              int index = HelperAddon.getInstance().getChatGuiModule().getTabbedPane()
                  .getSelectedIndex();
              if (index >= 0) {
                String title = HelperAddon.getInstance().getChatGuiModule().getTabbedPane()
                    .getTitleAt(index);
                if (title.contains(" ")) {
                  title = title.split(" ")[0];
                }
                title = title.trim();
                if (title.equals(username)) {
                  return;
                }
              }
            }

            String msgToSend = Settings.msgAutoAnswerText;

            // Replace
            msgToSend = msgToSend.replace("{player}", LabyMod.getInstance().getPlayerName());
            msgToSend = msgToSend.replace("{target}", username);
            msgToSend = msgToSend.replace("{message}", message);

            if (Minecraft.getInstance().player != null) {
              Minecraft.getInstance().player.sendChatMessage("/msg " + username + " " + msgToSend);
            }
          }
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

        if (action) {
          if (Settings.msgOpenGuiOnMessage) {
              //&& !HelperAddon.getInstance().getChatGuiModule().isVisible()) {
            // HelperAddon.getInstance().getChatGuiModule().setVisible(true);
          }
        }
      });
    }
  }

}
