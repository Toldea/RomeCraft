package toldea.romecraft.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.client.model.ModelRomanAnvil;
import toldea.romecraft.item.IRomeCraftItem;
import toldea.romecraft.item.crafting.RomanAnvilRecipes;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;

public class RenderTileEntityRomanAnvil extends TileEntitySpecialRenderer {
	private final ModelRomanAnvil modelRomanAnvil;
	private static final ItemRenderer itemRenderer = new ItemRenderer(Minecraft.getMinecraft());
	private static Icon iconIronBloom = ItemManager.itemIronBloom.getIconFromDamage(0);
	private static final ResourceLocation romanAnvilTexture = new ResourceLocation("romecraft", "textures/entity/romananvil.png");
	private static final ResourceLocation ironBloomTexture = new ResourceLocation("romecraft", "textures/items/ironbloom.png");

	public RenderTileEntityRomanAnvil() {
		modelRomanAnvil = new ModelRomanAnvil();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f) {
		if (tileEntity instanceof TileEntityRomanAnvil) {
			TileEntityRomanAnvil romanAnvil = (TileEntityRomanAnvil) tileEntity;

			GL11.glPushMatrix();
			GL11.glTranslatef((float) d0, (float) d1, (float) d2);
			
			GL11.glTranslatef(.5f, .5f, .5f);
			GL11.glRotatef(180f, 0F, 0F, 1F);

			bindTexture(romanAnvilTexture);
			modelRomanAnvil.renderAnvil(.0625f);
			
			GL11.glRotatef(-180f, 0F, 0F, 1F);
			GL11.glTranslatef(-.5f, -.5f, -.5f);

			if (romanAnvil.hasIronBloom()) {
				renderIronBlooms(romanAnvil.getIronBloomCount());
				if (romanAnvil.didRecentlyChangeIronBloomCount()) {
					ItemStack phantomItemStack = RomanAnvilRecipes.instance().getRecipeResult(romanAnvil.getStackInSlot(0));
					if (phantomItemStack != null) {
						renderPhantomFinishedItem(phantomItemStack.getItem(), romanAnvil.getNewIronBloomTimer(), TileEntityRomanAnvil.NEW_IRON_BLOOM_TIME);
					}
				}
			} else if (romanAnvil.hasFinishedItem()) {
				renderFinishedItem(romanAnvil.getFinishedItemStack().getItem());
			}
			
			GL11.glPopMatrix();
		}
	}

	private void renderIronBlooms(int count) {
		// Lazy alloc the icon.
		if (iconIronBloom == null) {
			iconIronBloom = ItemManager.itemIronBloom.getIconFromDamage(0);
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(0f, 1f, 0f);
		GL11.glRotatef(90f, 1F, 0F, 0F);
		GL11.glScalef(.5f, .5f, .5f);
		bindTexture(ironBloomTexture);

		// Dynamically position and draw the iron blooms based on the stack size.
		switch (count) {
		case 1:
			GL11.glTranslatef(.5f, .5f, 0f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			break;
		case 2:
			GL11.glTranslatef(.5f, 0f, 0f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, 1f, 0f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			break;
		case 3:
			GL11.glTranslatef(.5f, 0f, 0f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, .5f, -.01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, .5f, .01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			break;
		case 4:
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, 1f, 0f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(1f, 0f, 0f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, -1f, 0f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			break;
		case 5:
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, .6f, -.01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);

			GL11.glTranslatef(.5f, .4f, -.01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);

			GL11.glTranslatef(.5f, -.4f, .01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, -.6f, .01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			break;
		case 6:
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0, .5f, .01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, .5f, .01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(1f, 0f, 0f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, -.5f, -.01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			GL11.glTranslatef(0f, -.5f, -.01f);
			itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, iconIronBloom.getIconWidth(), iconIronBloom.getIconHeight(), 0.0625f);
			break;
		default:
			break;
		}
		
		GL11.glPopMatrix();
	}
	
	private void renderPhantomFinishedItem(Item phantomItem, int timerCurrent, int timerMax) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0f, 1.1f, 0f);
		GL11.glRotatef(90f, 1F, 0F, 0F);
		
		GL11.glEnable(GL11.GL_BLEND);
        
        float alpha = .8f;
        if (timerCurrent < timerMax / 2) {
        	alpha = (timerCurrent / (timerMax / 2f) * .8f);
        }
        GL11.glColor4f(1f, 1f, 1f, alpha);
		
        Icon icon = phantomItem.getIconFromDamage(0);
		bindTexture(((IRomeCraftItem)phantomItem).getResourceLocation());
		itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, icon.getIconWidth(), icon.getIconHeight(), 0.0625f);
		
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	private void renderFinishedItem(Item finishedItem) {
		if (!(finishedItem instanceof IRomeCraftItem)) {
			return;
		}
		
		Icon icon = finishedItem.getIconFromDamage(0);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0f, 1f, 0f);
		GL11.glRotatef(90f, 1F, 0F, 0F);
		bindTexture(((IRomeCraftItem)finishedItem).getResourceLocation());
		itemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, icon.getIconWidth(), icon.getIconHeight(), 0.0625f);
		GL11.glPopMatrix();
	}
}
