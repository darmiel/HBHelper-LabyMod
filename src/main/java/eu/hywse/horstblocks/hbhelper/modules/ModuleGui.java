package eu.hywse.horstblocks.hbhelper.modules;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import net.labymod.gui.elements.Tabs;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class ModuleGui extends GuiScreen {

    @Override
    public void initGui() {
        super.initGui();

        Tabs.initGuiScreen(this.buttonList, this);

        int lastY = 60;
        for (int i = 0; i < HelperAddon.getInstance().getModules().size(); i++) {
            Module module = HelperAddon.getInstance().getModules().get(i);
            this.buttonList.add(new GuiButton(i, width / 2 - 50, lastY, 100, 20, module.moduleName()));

            lastY += 30;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();

        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawOverlayBackground(0, 41);
        draw.drawOverlayBackground(this.height - 32, this.height);
        draw.drawGradientShadowTop(41.0D, 0.0D, (double)this.width);
        draw.drawGradientShadowBottom((double)(this.height - 32), 0.0D, (double)this.width);
        draw.drawCenteredString(ModColor.createColors("&c&lHorstBlocks Helper &8[&6Modules&8]"), (double)(this.width / 2), 29.0D);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        if (button.id >= 0 && button.id < HelperAddon.getInstance().getModules().size()) {
            Module module = HelperAddon.getInstance().getModules().get(button.id);
            module.onClick();
        }

        Tabs.actionPerformedButton(button);

        super.actionPerformed(button);
    }

}
