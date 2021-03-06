package toldea.romecraft.managers;

import java.lang.reflect.Field;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.world.WorldEvent;
import toldea.romecraft.client.renderer.RenderScutum;
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
	
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
		event.manager.addSound("romecraft:hammer_use.ogg");
	}
	
	private RenderScutum renderScutum = new RenderScutum();
	@ForgeSubscribe
	public void onRenderSpecials(RenderPlayerEvent.Specials.Post event) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		EntityPlayer player = event.entityPlayer;
		ItemStack heldItem = player.getHeldItem();
		RenderPlayer renderer = event.renderer;
		
		if (heldItem != null) { 
			boolean shouldRenderScutum = (heldItem.itemID == ItemManager.itemScutum.itemID);
			if (!shouldRenderScutum) {
				if (heldItem.itemID == ItemManager.itemGladius.itemID || heldItem.itemID == ItemManager.itemPilum.itemID) {
					int hotbarSlot = player.inventory.currentItem;
					int itemSlot = hotbarSlot + 1;
					ItemStack nearbyStack = null;
					if (hotbarSlot < 8) {
						nearbyStack = player.inventory.getStackInSlot(itemSlot);
						if (nearbyStack != null && nearbyStack.itemID == ItemManager.itemScutum.itemID) {
							shouldRenderScutum = true;
						}
					}
				}
			}
			
			if (shouldRenderScutum) {
				Field field = RenderPlayer.class.getDeclaredField("modelBipedMain");
				field.setAccessible(true);
				Object value = field.get(renderer);
				if (value instanceof ModelBiped) {
					ModelBiped bipedMain = (ModelBiped)value;
					renderScutum.renderThirdPersonScutum(player, bipedMain.bipedLeftArm);
				}
			}
		}
	}
	
	/*@ForgeSubscribe
	public void onRenderPlayerPre(RenderPlayerEvent.Pre event) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
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