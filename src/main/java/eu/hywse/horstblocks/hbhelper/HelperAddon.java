package eu.hywse.horstblocks.hbhelper;

import eu.hywse.horstblocks.hbhelper.modules.Module;
import eu.hywse.horstblocks.hbhelper.modules.ModuleGui;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.ChatGuiModule;
import eu.hywse.horstblocks.hbhelper.modules.chatgui.listener.PrivateChatListener;
import lombok.Getter;
import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.Tabs;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.ModColor;

import javax.swing.*;
import java.util.LinkedList;
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
     * Modules
     */
    @Getter
    private LinkedList<Module> modules = new LinkedList<>();

    @Getter
    private ChatGuiModule chatGuiModule;

    public HelperAddon() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getApi().getEventManager().register(new PrivateChatListener());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("ERR: " + e.getMessage());
        }
        modules.add(this.chatGuiModule = new ChatGuiModule());

        // Tabs
        // noinspection unchecked
        Tabs.getTabUpdateListener().add(map -> map.put("HorstBlocks Helper", new Class[] {ModuleGui.class}));
    }

    @Override
    public void loadConfig() {

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement(ModColor.createColors(ADDON_PREFIX + ADDON_VERSION + " &8| &bhyWse")));

        for (Module module : modules) {
            System.out.println("Loading module: " + module.moduleName());
            list.add(new HeaderElement(ModColor.createColors("&8» &7Module: &c" + module.moduleName())));
        }
    }

}
