package toldea.romecraft.utility;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RayTracer {
	private static Minecraft mc = Minecraft.getMinecraft();
	private static Entity pointedEntity;

	public static MovingObjectPosition blockRayTrace(EntityPlayer entityplayer, World world, double distance) {
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

		Vec3 vec3d1 = vec3d.addVector((double) f7 * distance, (double) f8 * distance, (double) f9 * distance);
		
		return world.rayTraceBlocks_do_do(vec3d, vec3d1, false, true);
	}
	
	public static MovingObjectPosition entityRayTrace(float par1, double distance) {
		MovingObjectPosition objectMouseOver = null;

		if (mc.renderViewEntity != null) {
			if (mc.theWorld != null) {
				double d0 = distance;
				objectMouseOver = mc.renderViewEntity.rayTrace(d0, par1);
				double d1 = d0;
				Vec3 vec3 = mc.renderViewEntity.getPosition(par1);

				if (objectMouseOver != null) {
					d1 = objectMouseOver.hitVec.distanceTo(vec3);
				}

				Vec3 vec31 = mc.renderViewEntity.getLook(par1);
				Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
				pointedEntity = null;
				float f1 = 1.0F;
				List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
						mc.renderViewEntity,
						mc.renderViewEntity.boundingBox.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double) f1, (double) f1,
								(double) f1));
				double d2 = d1;

				for (int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity) list.get(i);

					if (entity.canBeCollidedWith()) {
						float f2 = entity.getCollisionBorderSize();
						AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double) f2, (double) f2, (double) f2);
						MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

						if (axisalignedbb.isVecInside(vec3)) {
							if (0.0D < d2 || d2 == 0.0D) {
								pointedEntity = entity;
								d2 = 0.0D;
							}
						} else if (movingobjectposition != null) {
							double d3 = vec3.distanceTo(movingobjectposition.hitVec);

							if (d3 < d2 || d2 == 0.0D) {
								if (entity == mc.renderViewEntity.ridingEntity && !entity.canRiderInteract()) {
									if (d2 == 0.0D) {
										pointedEntity = entity;
									}
								} else {
									pointedEntity = entity;
									d2 = d3;
								}
							}
						}
					}
				}

				if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
					objectMouseOver = new MovingObjectPosition(pointedEntity);
				}
			}
		}
		return objectMouseOver;
	}
}
