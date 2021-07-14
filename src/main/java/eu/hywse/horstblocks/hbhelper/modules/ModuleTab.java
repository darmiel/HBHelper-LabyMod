package eu.hywse.horstblocks.hbhelper.modules;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import eu.hywse.horstblocks.hbhelper.HelperAddon;
import java.util.Map;
import javax.annotation.Nonnull;
import net.labymod.gui.elements.Tabs;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class ModuleTab extends Screen {

  private class MetaButton extends Button {

    private Map<String, Object> meta;

    public MetaButton(
        final int x,
        final int y,
        final int width,
        final int height,
        final String title,
        final IPressable pressedAction) {

      super(x, y, width, height, ITextComponent.getTextComponentOrEmpty(title), pressedAction);

      this.meta = Maps.newHashMap();
      this.meta.put("orig", title);
    }

    public String getOriginalMessage() {
      return this.meta.getOrDefault("orig", "").toString();
    }

    public void setMessage(final String title) {
      this.setMessage(ITextComponent.getTextComponentOrEmpty(title));
    }
  }

  private double bgX = 0.0;

  public ModuleTab() {
    super(ITextComponent.getTextComponentOrEmpty(""));
  }

  @Override
  public void init() {
    this.buttons.clear();

    int lastY = 60;
    for (int i = 0; i < HelperAddon.getInstance().getModules().size(); i++) {
      final Module module = HelperAddon.getInstance().getModules().get(i);

      // Widgets
      this.addButton(new MetaButton(
          width / 2 - 50,
          lastY,
          100,
          20,
          module.moduleName(),
          (press) -> module.onClick())
      );
      //

      lastY += 30;
    }

    Tabs.initGui(this);
  }

  //////

  @Override
  public void render(@Nonnull final MatrixStack matrixStack, final int mouseX, final int mouseY,
      final float partialTicks) {

    if (this.bgX > Double.MAX_VALUE - 100) {
      this.bgX = 0;
    }

    final DrawUtils draw = LabyMod.getInstance().getDrawUtils();

    // Background
    draw.drawAutoDimmedBackground(matrixStack, this.bgX += 0.375); // scrolling background
    draw.drawOverlayBackground(matrixStack, 0, 41);
    draw.drawGradientShadowTop(matrixStack, 41.0D, 0.0D, this.width);
    draw.drawOverlayBackground(matrixStack, this.height - 40, this.height);
    draw.drawGradientShadowBottom(matrixStack, this.height - 40, 0.0D, this.width);

    // Header
    draw.drawCenteredString(matrixStack,
        ModColor.createColors("&c&lHorstBlocks Helper &8[&6Modules&8]"),
        this.width / 2.0,
        27.0D);

    Tabs.drawScreen(this, matrixStack, mouseX, mouseY, partialTicks);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
  }

  //////

  @Override
  public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(final char codePoint, final int modifiers) {
    return super.charTyped(codePoint, modifiers);
  }

  @Override
  public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
    Tabs.mouseClicked(this);
    return super.mouseClicked(mouseX, mouseY, button);
  }
}