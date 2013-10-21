package toldea.romecraft.managers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.world.WorldEvent;
import toldea.romecraft.entity.ai.SquadManager;

public class EventManager {
	public static void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new EventManager());
	}

	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties("SquadManagerProperties", SquadManager.instance);
		}
	}

	@ForgeSubscribe
	public void onWorldUnload(WorldEvent.Unload event) {
		SquadManager.onWorldUnload();
	}

	/*
	@ForgeSubscribe
	public void onRenderPlayerPre(RenderPlayerEvent.Pre event) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		// event.setCanceled(true);
		EntityPlayer player = event.entityPlayer;
		ItemStack heldItem = player.getHeldItem();
		RenderPlayer renderer = event.renderer;
		if (heldItem != null && heldItem.itemID == ItemManager.itemPilum.itemID) {
			//event.setCanceled(true);
			Field field = RenderPlayer.class.getDeclaredField("modelBipedMain");
			field.setAccessible(true);
			Object value = field.get(renderer);
			if (value instanceof ModelBiped) {
				ModelBiped bipedMain = (ModelBiped)value;
				
				GL11.glPushMatrix();
				
				float f6 = 0.0F;
				float f7 = 0.0F;
				float par3 = 0.0f;
				bipedMain.bipedRightArm.rotateAngleZ = -((float)Math.PI / 16);
				bipedMain.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F) + bipedMain.bipedHead.rotateAngleY;
				bipedMain.bipedRightArm.rotateAngleX = -((float)Math.PI) + bipedMain.bipedHead.rotateAngleX;
				bipedMain.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				bipedMain.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
				bipedMain.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
				
				bipedMain.render(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
				GL11.glPopMatrix();
				
				field.set(renderer, value);
			}
		}
	}*/
}