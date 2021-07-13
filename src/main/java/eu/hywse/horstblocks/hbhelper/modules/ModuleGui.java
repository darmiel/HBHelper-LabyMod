package eu.hywse.horstblocks.hbhelper.modules;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class ModuleGui extends Screen {

  protected ModuleGui() {
    super(ITextComponent.getTextComponentOrEmpty(""));
  }

  @Override
  protected void init() {
    int lastY = 60;
    for (int i = 0; i < HelperAddon.getInstance().getModules().size(); i++) {
      final Module module = HelperAddon.getInstance().getModules().get(i);

      // Widgets
      this.addButton(new Button(
          width / 2 - 50,
          lastY,
          100,
          20,
          ITextComponent.getTextComponentOrEmpty(module.moduleName()),
          (press) -> module.onClick())
      );
      //

      lastY += 30;
    }
  }

}
