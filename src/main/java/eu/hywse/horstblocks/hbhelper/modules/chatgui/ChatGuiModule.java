package eu.hywse.horstblocks.hbhelper.modules.chatgui;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.modules.Module;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.gui.UserChat;
import eu.hywse.horstblocks.hbhelper.utils.ResourceUtil;
import lombok.Getter;
import net.labymod.main.LabyMod;
import net.minecraft.client.resources.IResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatGuiModule extends JFrame implements Module {

    @Getter
    private static Map<String, UserChat> chats = new HashMap<>();

    @Getter
    private JTabbedPane tabbedPane;

    public static final IResource TEXTURE_HEAD_128 = ResourceUtil.getResource("icons/horst_head.png");
    public static final IResource TEXTURE_SEND = ResourceUtil.getResource("icons/send_icon.png");

    public ChatGuiModule() {
        add(tabbedPane = new JTabbedPane(SwingConstants.TOP));

        System.out.println(TEXTURE_HEAD_128);
        System.out.println(TEXTURE_HEAD_128.getInputStream());

        // Listeners
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() instanceof UserChat) {
                UserChat chat = (UserChat) tabbedPane.getSelectedComponent();
                chat.updateTitle();
            }
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
        try {
            setIconImage(ImageIO.read(TEXTURE_HEAD_128.getInputStream()));
        } catch (IOException e) {
            System.out.println("Konnte Icon nicht laden: " + e.getMessage());
        }
    }

    public static UserChat getChat(String username) {
        if (!chats.containsKey(username)) {
            UserChat chat;
            try {
                chat = new UserChat(username, HelperAddon.getInstance().getChatGuiModule().getTabbedPane());
            } catch (IOException e) {
                return null;
            }
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
        setTitle("HB-Helper BETA (Module: MSG) | [Eingeloggt als " + LabyMod.getInstance().getPlayerName() + "]");
    }

}
