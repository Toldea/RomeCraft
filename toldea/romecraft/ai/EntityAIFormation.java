package toldea.romecraft.ai;

import java.io.Console;
import java.util.List;

import toldea.romecraft.entity.EntityLegionary;

import net.minecraft.client.Minecraft;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIFormation extends EntityAIBase {
	World worldObj;
	EntityLegionary legionary;

	private float distanceFromEntity;

	private double xPosition;
	private double yPosition;
	private double zPosition;
	private double speed;

	public EntityAIFormation(EntityLegionary par1Legionary, double par2Speed) {
		legionary = par1Legionary;
		worldObj = legionary.worldObj;
		speed = par2Speed;
	}

	@Override
	public boolean shouldExecute() {
		if (!legionary.isRegistered()) {
			return false;
		}
		int contuberniumId = legionary.getContuberniumId();
		Contubernium contubernium = SquadManager.getContubernium(contuberniumId);
		Vec3 vec3 = contubernium.getTargetLocation();

		if (vec3 == null) {
			return false;
		} else {
			float offset = SquadManager.getFormationOffsetForContubernium(contubernium);
			this.xPosition = vec3.xCoord + (legionary.getSquadIndex() % Contubernium.files * Contubernium.tightness) - (offset * Contubernium.tightness);
			this.yPosition = vec3.yCoord;
			this.zPosition = vec3.zCoord + ((int)(legionary.getSquadIndex() / Contubernium.files) * Contubernium.tightness);
			return true;
		}
	}

	public boolean continueExecuting() {
		return !this.legionary.getNavigator().noPath();
	}

	public void updateTask() {
		this.legionary.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
	}
}
