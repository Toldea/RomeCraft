package toldea.romecraft.client.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;

import toldea.romecraft.client.model.ModelScutum;
import toldea.romecraft.entity.EntityPilum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

public class RenderScutum extends Render implements IItemRenderer {
	private static final ResourceLocation scutumTexture = new ResourceLocation("romecraft", "textures/models/armor/scutum.png");
	private ModelScutum modelScutum;
	
	public RenderScutum() {
		super();
		modelScutum = new ModelScutum();
		this.setRenderManager(RenderManager.instance);
	}
	
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return scutumTexture;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return false;//(type == ItemRenderType.EQUIPPED_FIRST_PERSON);
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			//renderScutum((Entity)data[1]);
		}
	}
	
	public void renderScutum(Entity entity) {
		GL11.glPushMatrix();
		
		//GL11.glRotatef(180f, 1f, 0f, 0f);
		//GL11.glRotatef(45f, 0f, 0f, 1f);
		
		//GL11.glTranslatef(0f, 0f, -1f);
		
		bindTexture(scutumTexture);
		
		this.modelScutum.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glPopMatrix();
	}
}
