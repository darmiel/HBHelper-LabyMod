package eu.hywse.horstblocks.hbhelper.modules.chatgui;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.modules.Module;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.gui.UserChat;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import lombok.Getter;
import net.labymod.main.LabyMod;
import net.minecraft.resources.IResource;

public class ChatGuiModule extends JFrame implements Module {

  @Getter
  private static final Map<String, UserChat> chats = new HashMap<>();

  @Getter
  private JTabbedPane tabbedPane;

  public ChatGuiModule() {
    add(tabbedPane = new JTabbedPane(SwingConstants.TOP));

    // Listeners
    tabbedPane.addChangeListener(e -> {
      if (tabbedPane.getSelectedComponent() instanceof UserChat) {
        UserChat chat = (UserChat) tabbedPane.getSelectedComponent();
        chat.updateTitle();
      }

      updateTitle();
    });

    setMinimumSize(new Dimension(400, 200));

    if (tabbedPane.getTabCount() == 0) {
      setSize(800, 400);
    }

    addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        updateTitle();
      }

      @Override
      public void focusLost(FocusEvent e) {
        updateTitle();
      }
    });
    updateTitle();
  }

  public static UserChat getChat(String username) {
    if (!chats.containsKey(username)) {
      UserChat chat = new UserChat(username,
          HelperAddon.getInstance().getChatGuiModule().getTabbedPane());
      chat.open();

      chats.put(username, chat);
    }

    return chats.getOrDefault(username, null);
  }

  public static boolean hasChat(String username) {
    return chats.containsKey(username);
  }

  @Override
  public void onClick() {
    setVisible(!isVisible());
  }

  @Override
  public String moduleName() {
    return "ChatGUI";
  }

  private void updateTitle() {
    setTitle(
        "HB-Helper BETA (Module: MSG) | [Eingeloggt als " + LabyMod.getInstance().getPlayerName()
            + "]");
  }

}
