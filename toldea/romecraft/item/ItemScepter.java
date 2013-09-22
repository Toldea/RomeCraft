package toldea.romecraft.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import toldea.romecraft.ai.SquadManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemScepter extends GenericItem {
	public ItemScepter(int id) {
		super(id);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World world, EntityPlayer entityplayer, int par4) {
		float f1 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * 1.0f;
		float f2 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * 1.0f;
		double d = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * 1.0;
		double d1 = (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * 1.0 + 1.6200000000000001D) - (double) entityplayer.yOffset;
		double d2 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * 1.0;
		Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
		float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
		float f5 = -MathHelper.cos(-f1 * 0.01745329F);
		float f6 = MathHelper.sin(-f1 * 0.01745329F);
		float f7 = f4 * f5;
		float f8 = f6;
		float f9 = f3 * f5;
		double d3 = 5000D;

		Vec3 vec3d1 = vec3d.addVector((double) f7 * d3, (double) f8 * d3, (double) f9 * d3);

		MovingObjectPosition blockRayTrace = world.rayTraceBlocks_do_do(vec3d, vec3d1, false, true);

		if (blockRayTrace != null && blockRayTrace.typeOfHit == EnumMovingObjectType.TILE) {
			//System.out.println("Tile Selected: " + blockRayTrace.hitVec);
			SquadManager.giveMovementOrder(1, blockRayTrace.hitVec);
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
