package toldea.romecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class GuiItemButton extends GuiButton {
	private RenderItem renderItem = new RenderItem();
	private final ItemStack itemStack;
	
	public GuiItemButton(int par1, int par2, int par3, int par4, int par5, String par6Str, ItemStack itemStack) {
		super(par1, par2, par3, par4, par5, par6Str);
		this.itemStack = itemStack;
	}
	
	public void drawButtonImage(Minecraft minecraft) {
		renderItem.renderItemIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), itemStack, xPosition + 2, yPosition + 2);
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
}
