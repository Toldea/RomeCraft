package toldea.romecraft.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.item.crafting.RomanAnvilRecipes;
import toldea.romecraft.item.crafting.RomanAnvilRecipes.AnvilRecipe;

public class GuiBlacksmith extends RomeCraftGuiScreen {
	private static final ResourceLocation texture = new ResourceLocation("romecraft", "textures/gui/gui_blacksmith.png");

	private static final int BUTTON_WIDTH = 20;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_HORIZONTAL_SPACING = 0;

	public GuiBlacksmith(EntityPleb blacksmithPleb) {
		
	}
	
	public void initGui() {
		this.xSize = 248;
		this.ySize = 166;
		super.initGui();
	}

	@Override
	public void initGuiComponents() {
		List<AnvilRecipe> anvilRecipes = RomanAnvilRecipes.instance().getRecipeList();
		for (int i = 0; i < anvilRecipes.size(); i++) {
			AnvilRecipe recipe = anvilRecipes.get(i);
			this.buttonList.add(new GuiItemButton(i, guiLeft + i * (BUTTON_WIDTH + BUTTON_HORIZONTAL_SPACING), guiTop, BUTTON_WIDTH, BUTTON_WIDTH, "",
					recipe.craftedItem));
		}
	}

	@Override
	public void drawBackground(int par1) {
		super.drawWorldBackground(0);
		GL11.glColor4f(1, 1, 1, 1);
		minecraft.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void drawForeground() {
		for (int i = 0; i < this.buttonList.size(); i++) {
			GuiButton guiButton = (GuiButton) this.buttonList.get(i);
			if (guiButton instanceof GuiItemButton) {
				((GuiItemButton) guiButton).drawButtonImage(minecraft);
				fontRenderer.drawString("0", guiLeft + 7 + i * (BUTTON_WIDTH + BUTTON_HORIZONTAL_SPACING), guiTop + 22, 0x404040);
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton instanceof GuiItemButton) {
			ItemStack itemStack = ((GuiItemButton)guiButton).getItemStack();
		}
	}
}
