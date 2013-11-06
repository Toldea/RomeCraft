package toldea.romecraft.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import toldea.romecraft.managers.ItemManager;

public class RenderItemGladius implements IItemRenderer {
	private static final ItemRenderer itemRenderer = new ItemRenderer(Minecraft.getMinecraft());
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private static final RenderScutum renderScutum = new RenderScutum();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return (type == ItemRenderType.EQUIPPED_FIRST_PERSON);
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON && data[1] instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) data[1];

			// Render the gladius 'like normal'
			renderFirstPersonGladius(player, item, 0);

			// Check if the item to the right of the gladius in the player's inventory is a scutum. If so, also render the scutum.
			int hotbarSlot = player.inventory.currentItem;
			int itemSlot = hotbarSlot + 1;
			ItemStack nearbyStack = null;
			if (hotbarSlot < 8) {
				nearbyStack = player.inventory.getStackInSlot(itemSlot);
				if (nearbyStack != null && nearbyStack.itemID == ItemManager.itemScutum.itemID) {
					renderScutum.renderFirstPersonScutum(player);
				}

			}
		}
	}

	/**
	 * Custom render function that is basically a copy of ItemRenderer.renderItem with some transformations removed. This because forge does these
	 * transformations just before it calls a custom IItemRenderer so otherwise these would be performed twice.
	 */
	public void renderFirstPersonGladius(EntityLivingBase par1EntityLivingBase, ItemStack par2ItemStack, int par3) {
		GL11.glPushMatrix();
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();

		Icon icon = par1EntityLivingBase.getItemIcon(par2ItemStack, par3);
		if (icon == null) {
			GL11.glPopMatrix();
			return;
		}

		texturemanager.bindTexture(texturemanager.getResourceLocation(par2ItemStack.getItemSpriteNumber()));
		Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		itemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(),
				0.0625F);

		if (par2ItemStack.hasEffect(par3)) {
			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDisable(GL11.GL_LIGHTING);
			texturemanager.bindTexture(RES_ITEM_GLINT);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
			float f7 = 0.76F;
			GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glPushMatrix();
			float f8 = 0.125F;
			GL11.glScalef(f8, f8, f8);
			float f9 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
			GL11.glTranslatef(f9, 0.0F, 0.0F);
			GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
			itemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(f8, f8, f8);
			f9 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
			GL11.glTranslatef(-f9, 0.0F, 0.0F);
			GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
			itemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

		GL11.glPopMatrix();
	}
}
