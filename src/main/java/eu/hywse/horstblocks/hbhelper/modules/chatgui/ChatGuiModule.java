package eu.hywse.horstblocks.hbhelper.modules.chatgui;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.modules.Module;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.gui.UserChat;
import lombok.Getter;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ChatGuiModule extends JFrame implements Module {

    @Getter
    private static Map<String, UserChat> chats = new HashMap<>();

    @Getter
    private JTabbedPane tabbedPane;

    public ChatGuiModule() {
        add(tabbedPane = new JTabbedPane(SwingConstants.TOP));
        pack();

        // Listeners
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() instanceof UserChat) {
                UserChat chat = (UserChat) tabbedPane.getSelectedComponent();
                chat.updateTitle();
            }
        });

        if (tabbedPane.getTabCount() == 0) {
            setSize(800, 400);
        }
    }

    public static UserChat getChat(String username) {
        if (!chats.containsKey(username)) {
            UserChat chat = new UserChat(username, HelperAddon.getInstance().getChatGuiModule().getTabbedPane());
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

}
