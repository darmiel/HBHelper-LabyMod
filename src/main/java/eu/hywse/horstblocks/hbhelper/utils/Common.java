package eu.hywse.horstblocks.hbhelper.utils;

import static eu.hywse.horstblocks.hbhelper.HelperAddon.ADDON_PREFIX;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;

public class Common {

  public static void addChatMessage(String msg) {
    addChatMessage(msg, true);
  }

  /**
   * Sends the player a message + prefix (TextComponent)
   *
   * @param msg Message
   */
  public static void addChatMessage(String msg, boolean prefix) {
    String message = ModColor.createColors((prefix ? ADDON_PREFIX : "") + msg);

    if (LabyMod.getInstance().isInGame()) {
      HelperAddon.getInstance().getApi().displayMessageInChat(message);
      return;
    }

    System.out.println(ModColor.removeColor(msg));
  }

}
