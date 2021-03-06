package toldea.romecraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import toldea.romecraft.command.EntitySelectorLegionary;
import toldea.romecraft.entity.ai.SquadManager;
import toldea.romecraft.utility.RayTracer;

public class ItemScepter extends RomeCraftItem {
	public ItemScepter(int id) {
		super(id);
		this.setMaxStackSize(1);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World world, EntityPlayer entityplayer, int par4) {
		double distance = 5000D;

		MovingObjectPosition entityRayTrace = RayTracer.entityRayTrace(1.0f, distance);
		if (entityRayTrace != null && entityRayTrace.typeOfHit == EnumMovingObjectType.ENTITY) {
			System.out.println("Entity Selected: " + entityRayTrace.entityHit);

			Entity entity = entityRayTrace.entityHit;
			if (EntitySelectorLegionary.instance.isEntityApplicable(entity)) {
				SquadManager.giveAttackOrder(1, (EntityLivingBase) entity);
				return;
			}
		}

		MovingObjectPosition blockRayTrace = RayTracer.blockRayTrace(entityplayer, world, distance);
		if (blockRayTrace != null && blockRayTrace.typeOfHit == EnumMovingObjectType.TILE) {
			System.out.println("Tile Selected: " + blockRayTrace.hitVec);
			SquadManager.giveMovementOrder(1, blockRayTrace.hitVec);
			return;
		}
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			SquadManager.giveMovementOrder(1, null);
		} else {
			entityplayer.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
		}
		return itemstack;
	}
}
