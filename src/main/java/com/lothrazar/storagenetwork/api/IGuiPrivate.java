package com.lothrazar.storagenetwork.api;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IGuiPrivate {

  void renderStackToolTip(ItemStack stack, int x, int y);

  void renderTooltip(List<String> t, int x, int y);

  void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor);

  int getGuiTop();

  int getGuiLeft();

  boolean isInRegion(int x, int y, int width, int height, double mouseX, double mouseY);
}
