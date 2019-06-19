package eu.hywse.horstblocks.hbhelper;

import eu.hywse.horstblocks.hbhelper.modules.chatgui.MainChatGui;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.PrivateChatListener;
import lombok.Getter;
import lombok.Setter;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelperAddon extends LabyModAddon {

    public static final String ADDON_VERSION = "B-1.0.1";
    public static final String ADDON_PREFIX = "  &c&lHelper &8» &7";

    @Getter
    private static HelperAddon instance;

    @Getter
    private static ExecutorService service = Executors.newCachedThreadPool();

    /*
     * Module: ChatGUI
     */
    @Getter
    private MainChatGui mainChatGui = new MainChatGui();
    @Getter
    @Setter
    private boolean onlyLogWhenVisible;
    @Getter
    @Setter
    private boolean playSound;

    public HelperAddon() {
        instance = this;
    }

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

    @Override
    public void onEnable() {
        getApi().getEventManager().register(new PrivateChatListener());
    }

    @Override
    public void loadConfig() {
        setOnlyLogWhenVisible(getConfig().has("Only Log When GUI is visible") && getConfig().get("Only Log When GUI is visible").getAsBoolean());
        setPlaySound(!getConfig().has("Play Sound on unread messages") || getConfig().get("Play Sound on unread messages").getAsBoolean());
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement(ModColor.createColors(ADDON_PREFIX + ADDON_VERSION + " &8| &bhyWse")));
        list.add(new BooleanElement(
                "MSG nur bei offenem GUI loggen",
                new ControlElement.IconData(Material.PAPER),
                b -> {
                    setOnlyLogWhenVisible(b);
                    getConfig().addProperty("Only Log When GUI is visible", b);
                    saveConfig();
                },
                isOnlyLogWhenVisible()));
        list.add(new BooleanElement(
                "Ton bei ungelesenen Nachrichten",
                new ControlElement.IconData(Material.NOTE_BLOCK),
                b -> {
                    setOnlyLogWhenVisible(b);
                    getConfig().addProperty("Play Sound on unread messages", b);
                    saveConfig();
                },
                isPlaySound()));
    }

}
