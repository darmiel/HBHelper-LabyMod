package eu.hywse.horstblocks.hbhelper.modules.chatgui;

import eu.hywse.horstblocks.hbhelper.modules.chatgui.chatpanel.UserChat;
import lombok.Getter;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class MainChatGui extends JFrame {

    @Getter
    private static Map<String, UserChat> chats = new HashMap<>();

    @Getter
    private JTabbedPane tabbedPane;

    public MainChatGui() {
        add(tabbedPane = new JTabbedPane(SwingConstants.TOP));
        pack();

        // Listeners
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() instanceof UserChat) {
                UserChat chat = (UserChat) tabbedPane.getSelectedComponent();
                chat.updateTitle();
            }
        });
    }

    public UserChat getChat(String username) {
        if (!chats.containsKey(username)) {
            UserChat chat = new UserChat(username, tabbedPane);
            chat.open();
            chats.put(username, chat);
        }
        return chats.getOrDefault(username, null);
    }

    public boolean hasChat(String username) {
        return chats.containsKey(username);
    }

}
