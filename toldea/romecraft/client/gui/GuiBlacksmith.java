package toldea.romecraft.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.entity.ai.blacksmith.BlacksmithOrders;
import toldea.romecraft.item.crafting.RomanAnvilRecipes;
import toldea.romecraft.item.crafting.RomanAnvilRecipes.AnvilRecipe;
import toldea.romecraft.managers.PacketManager;

public class GuiBlacksmith extends RomeCraftGuiScreen {
	private static final ResourceLocation texture = new ResourceLocation("romecraft", "textures/gui/gui_blacksmith.png");

	private static final int BUTTON_WIDTH = 20;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_HORIZONTAL_SPACING = 0;

	private final EntityPleb blacksmithPleb;

	public GuiBlacksmith(EntityPleb blacksmithPleb) {
		this.blacksmithPleb = blacksmithPleb;
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
				GuiItemButton guiItemButton = (GuiItemButton) guiButton;
				guiItemButton.drawButtonImage(minecraft);
				int itemId = guiItemButton.getItemStack().itemID;
				int quantity = blacksmithPleb.getBlacksmithOrders().getOrderQuantityForItemId(itemId);
				fontRenderer.drawString("" + quantity, guiLeft + 8 + i * (BUTTON_WIDTH + BUTTON_HORIZONTAL_SPACING), guiTop + 22, 0x404040);
			}
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 1) {
			for (int l = 0; l < this.buttonList.size(); ++l) {
				GuiButton guibutton = (GuiButton) this.buttonList.get(l);

				if (guibutton.mousePressed(this.mc, par1, par2)) {
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.rightClickActionPerformed(guibutton);
				}
			}
		}
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton instanceof GuiItemButton) {
			GuiItemButton guiItemButton = (GuiItemButton) guiButton;
			ItemStack itemStack = guiItemButton.getItemStack();
			BlacksmithOrders orders = blacksmithPleb.getBlacksmithOrders();
			orders.adjustOrderQuantityForItemId(itemStack.itemID, 1);
			PacketManager.sendAdjustBlacksmithOrderQuantityPacketToServer(blacksmithPleb.entityId, itemStack.itemID, 1);
		}
	}

	protected void rightClickActionPerformed(GuiButton guiButton) {
		if (guiButton instanceof GuiItemButton) {
			GuiItemButton guiItemButton = (GuiItemButton) guiButton;
			ItemStack itemStack = guiItemButton.getItemStack();
			BlacksmithOrders orders = blacksmithPleb.getBlacksmithOrders();
			// Check if we have more than 0 items, if so reduce the quantity by 1.
			if (orders.getOrderQuantityForItemId(itemStack.itemID) > 0) {
				orders.adjustOrderQuantityForItemId(itemStack.itemID, -1);
				PacketManager.sendAdjustBlacksmithOrderQuantityPacketToServer(blacksmithPleb.entityId, itemStack.itemID, -1);
			}
		}
	}
}
