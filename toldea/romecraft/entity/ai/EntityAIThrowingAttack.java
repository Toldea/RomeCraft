package toldea.romecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.EntityLegionary.LEGIONARY_EQUIPMENT;

public class EntityAIThrowingAttack extends EntityAIBase {
	/**
	 * The entity (as a RangedAttackMob) the AI instance has been applied to.
	 */
	private final EntityLegionary legionaryEntityHost;
	private EntityLivingBase attackTarget;

	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private double entityMoveSpeed;
	private int field_96561_g;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private int maxRangedAttackTime;
	private float maxDistanceToTarget;
	private float maxDistanceToTargetSquared;

	public EntityAIThrowingAttack(EntityLegionary par1IRangedAttackMob, double par2, int par4, float par5) {
		this(par1IRangedAttackMob, par2, par4, par4, par5);
	}

	public EntityAIThrowingAttack(EntityLegionary par1IRangedAttackMob, double par2entityMoveSpeed, int par4, int par5maxRangedAttackTime, float par6maxDistanceToTarget) {
		this.rangedAttackTime = -1;

		if (!(par1IRangedAttackMob instanceof EntityLivingBase)) {
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		} else {
			this.legionaryEntityHost = par1IRangedAttackMob;
			this.entityMoveSpeed = par2entityMoveSpeed;
			this.field_96561_g = par4;
			this.maxRangedAttackTime = par5maxRangedAttackTime;
			this.maxDistanceToTarget = par6maxDistanceToTarget;
			this.maxDistanceToTargetSquared = par6maxDistanceToTarget * par6maxDistanceToTarget;
			this.setMutexBits(3);
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		// We obviously can't throw a Pilum if we don't have one left, so check that we do.
		if (!legionaryEntityHost.getPilumLeft()) {
			return false;
		}
		
		EntityLivingBase entitylivingbase = this.legionaryEntityHost.getAttackTarget();

		if (entitylivingbase == null || !entitylivingbase.isEntityAlive()) {
			return false;
		} else {
			legionaryEntityHost.equipItem(LEGIONARY_EQUIPMENT.PILUM);
			this.attackTarget = entitylivingbase;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.shouldExecute();
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.attackTarget = null;
		this.rangedAttackTime = -1;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		double d0 = this.legionaryEntityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
		boolean flag = this.legionaryEntityHost.getEntitySenses().canSee(this.attackTarget);

		this.legionaryEntityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		float f;

		if (--this.rangedAttackTime == 0) {
			if (d0 > (double) this.maxDistanceToTargetSquared || !flag) {
				return;
			}

			f = MathHelper.sqrt_double(d0) / this.maxDistanceToTarget;
			float f1 = f;

			if (f < 0.1F) {
				f1 = 0.1F;
			}

			if (f1 > 1.0F) {
				f1 = 1.0F;
			}

			this.legionaryEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
			this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime) + (float) this.field_96561_g);
		} else if (this.rangedAttackTime < 0) {
			f = MathHelper.sqrt_double(d0) / this.maxDistanceToTarget;
			this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime) + (float) this.field_96561_g);
		}
	}
}