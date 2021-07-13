package eu.hywse.horstblocks.hbhelper.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.SettingsElement;

public class TextElement extends SettingsElement {

  private final int height;
  private final int spaceTop;

  public TextElement(String text, int spaceTop, int hight) {
    super(text, null);
    this.spaceTop = spaceTop;
    this.height = hight;
  }

  @Override
  public void draw(
      final MatrixStack matrixStack,
      final int x,
      final int y,
      final int maxX,
      final int maxY,
      final int mouseX,
      final int mouseY) {

    super.draw(matrixStack, x, y, maxX, maxY, mouseX, mouseY);
    final int absoluteY = y + spaceTop;

    LabyMod.getInstance().getDrawUtils().drawString(matrixStack, super.getDisplayName(), x, absoluteY);
  }


  public int getEntryHeight() {
    return height;
  }

  public int getEntryWidth() {
    return 85;
  }

  /// Not implemented:

  @Override
  public void drawDescription(final int i, final int i1, final int i2) {}

  @Override
  public void mouseClicked(final int i, final int i1, final int i2) {}

  @Override
  public void mouseRelease(final int i, final int i1, final int i2) {}

  @Override
  public void mouseClickMove(final int i, final int i1, final int i2) {}

  @Override
  public void charTyped(final char c, final int i) { }

  @Override
  public void unfocus(final int i, final int i1, final int i2) { }

}