package toldea.romecraft.entity;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import toldea.romecraft.command.EntitySelectorLegionary;
import toldea.romecraft.entity.ai.Contubernium;
import toldea.romecraft.entity.ai.EntityAIChargeThrow;
import toldea.romecraft.entity.ai.EntityAIFormationLookForward;
import toldea.romecraft.entity.ai.EntityAIFormationMoveTowardsEntity;
import toldea.romecraft.entity.ai.EntityAIFormationMoveTowardsLocation;
import toldea.romecraft.entity.ai.EntityAIMeleeAttack;
import toldea.romecraft.entity.ai.SquadManager;
import toldea.romecraft.managers.ItemManager;

public class EntityLegionary extends EntityCreature implements INpc {
	public enum LEGIONARY_EQUIPMENT {
		LORICA_SEGMENTATA, GALEA, CINGULUM, CALIGAE, GLADIUS, PUGIO, SCUTUM, PILUM, VERUTUM, SARCINA, SUDIS, SHOVEL, WATERSKIN
	}

	private static final EntitySelectorLegionary enemySelector = EntitySelectorLegionary.instance;
	private static final float accuracy = 5f;
	private static final float pilumChargeRange = 15f;
	private static final float pilumRange = 20f;
	private static final float pathSearchRange = 64f; // TODO: revert this to 16 and implement better pathfinding
	public static final float enemySearchRange = 16f;
	private static final double movementSpeed = .6;
	private static final double meleeEngageRange = 8d;

	private int contuberniumId = 1;
	private int pilaLeft;
	private boolean registered = false;
	
	private boolean holdingScutum = false;

	public EntityLegionary(World par1World) {
		super(par1World);

		// this.experienceValue = 10;

		this.getNavigator().setCanSwim(true);
		this.getNavigator().setAvoidsWater(true);

		// this.setSneaking(true);

		// this.tasks.addTask(1, new EntityAIThrowingAttack(this, 1.0D, 20, 60, pilumRange));

		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIChargeThrow(this, 5, pilumChargeRange));
		this.tasks.addTask(2, new EntityAIMeleeAttack(this, movementSpeed, meleeEngageRange));

		// this.tasks.addTask(3, new EntityAIMeleeAttackOld(this));
		this.tasks.addTask(4, new EntityAIFormationMoveTowardsEntity(this, movementSpeed));
		this.tasks.addTask(5, new EntityAIFormationMoveTowardsLocation(this, movementSpeed));
		this.tasks.addTask(6, new EntityAIFormationLookForward(this));

		// this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		// this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
		// this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		// this.tasks.addTask(5, new EntityAILookAtVillager(this));
		// this.tasks.addTask(6, new EntityAIWander(this, movementSpeed));
		// this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		// this.tasks.addTask(8, new EntityAILookIdle(this));
		// this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
		// this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));

		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, enemySelector));
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataWatcher().addObject(20, (byte)0);
	}
	
	protected boolean isAIEnabled() {
		return true;
	}

	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		SquadManager.removeFromContubernium(this.contuberniumId, this);
	}

	public int getContuberniumId() {
		return contuberniumId;
	}

	public boolean isRegistered() {
		return registered;
	}

	public int getSquadIndex() {
		return SquadManager.getContubernium(contuberniumId).getSquadIndex(this);
	}

	public void onUpdate() {
		super.onUpdate();
		if (!this.worldObj.isRemote) {
			if (!registered) {
				SquadManager.registerToContubernium(this.contuberniumId, this);
				registered = true;
			}
		}
	}

	public EntityLivingData onSpawnWithEgg(EntityLivingData entityLivingData) {
		entityLivingData = super.onSpawnWithEgg(entityLivingData);

		pilaLeft = 1;

		// Give the Legionary its default equipment.
		equipItem(LEGIONARY_EQUIPMENT.LORICA_SEGMENTATA);
		equipItem(LEGIONARY_EQUIPMENT.GALEA);
		equipItem(LEGIONARY_EQUIPMENT.CALIGAE);
		equipItem(LEGIONARY_EQUIPMENT.CINGULUM);

		//equipItem(LEGIONARY_EQUIPMENT.GLADIUS);
		equipItem(LEGIONARY_EQUIPMENT.SCUTUM);
		equipItem(LEGIONARY_EQUIPMENT.PILUM);

		return entityLivingData;
	}

	public boolean getPilumLeft() {
		return (pilaLeft >= 1);
	}

	public int getPilumCount() {
		return pilaLeft;
	}

	public boolean isHoldingPilum() {
		ItemStack itemStack = getCurrentItemOrArmor(0);
		if (itemStack != null) {
			return (itemStack.itemID == ItemManager.itemPilum.itemID);
		} else {
			return false;
		}
	}
	
	public boolean isHoldingScutum() {
		holdingScutum = (this.getDataWatcher().getWatchableObjectByte(20) == (byte)1);
		return holdingScutum;
	}

	public void equipItem(LEGIONARY_EQUIPMENT equipment) {
		switch (equipment) {
		case LORICA_SEGMENTATA:
			equipItemToSlot(ItemManager.itemLoricaSegmentata, 3);
			break;
		case GALEA:
			equipItemToSlot(ItemManager.itemGalea, 4);
			break;
		case CINGULUM:
			equipItemToSlot(ItemManager.itemCingulum, 2);
			break;
		case CALIGAE:
			equipItemToSlot(ItemManager.itemCaligae, 1);
			break;
		case GLADIUS:
			equipItemToSlot(ItemManager.itemGladius, 0);
			break;
		case PUGIO:
			break;
		case SCUTUM:
			holdingScutum = true;
			if (!this.worldObj.isRemote) {
				this.getDataWatcher().updateObject(20, (byte)1);
			}
			break;
		case PILUM:
			equipItemToSlot(ItemManager.itemPilum, 0);
			break;
		case VERUTUM:
			break;
		case SARCINA:
			break;
		case SUDIS:
			break;
		case SHOVEL:
			break;
		case WATERSKIN:
			break;
		default:
			break;
		}
	}

	private void equipItemToSlot(Item item, int itemSlot) {
		// Make sure the item isn't already equipped.
		ItemStack currentEquippedItem = this.getCurrentItemOrArmor(itemSlot);
		if (currentEquippedItem == null || currentEquippedItem.itemID != item.itemID) {
			this.setCurrentItemOrArmor(itemSlot, new ItemStack(item));
		}
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		int i = 0;

		if (par1Entity instanceof EntityLivingBase) {
			f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) par1Entity);
			i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) par1Entity);
		}

		boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0) {
				par1Entity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F), 0.1D,
						(double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				par1Entity.setFire(j * 4);
			}

			if (par1Entity instanceof EntityLivingBase) {
				EnchantmentThorns.func_92096_a(this, (EntityLivingBase) par1Entity, this.rand);
			}
		}

		return flag;
	}

	public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float rangeDamagePenalty) {
		EntityPilum entityPilum = new EntityPilum(this.worldObj, this, par1EntityLivingBase, 1.6F, accuracy);
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
		// entityPilum.setDamage((double) (rangeDamagePenalty * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.difficultySetting *
		// 0.11F));
		entityPilum.setDamage(8d + this.rand.nextGaussian() * 4D);

		if (i > 0) {
			entityPilum.setDamage(entityPilum.getDamage() + (double) i * 0.5D + 0.5D);
		}

		if (j > 0) {
			entityPilum.setKnockbackStrength(j);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0) {
			entityPilum.setFire(100);
		}

		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(entityPilum);
		pilaLeft--;
		if (pilaLeft <= 0) {
			this.setCurrentItemOrArmor(0, null);
		}
	}

	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
		boolean flag = itemstack != null && itemstack.itemID == Item.monsterPlacer.itemID;

		if (!flag && this.isEntityAlive()) {
			if (!this.worldObj.isRemote) {
				Contubernium contubernium = SquadManager.getContubernium(contuberniumId);
				par1EntityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("Legionary! (Contubernium: " + contuberniumId + ", Size: "
						+ contubernium.getSquadSize() + ", Facing: " + contubernium.getFacing() + ", Squad Index: " + getSquadIndex() + ")"));
			}
			return true;
		} else {
			return super.interact(par1EntityPlayer);
		}
	}

	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		// Set it's 'followRange' to a much larger value as the default one.
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange).setAttribute(pathSearchRange);
		this.getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(3.0D);
	}

	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		pilaLeft = 1; // TODO: temporary cheat to easily reload legionary ammo.

		compound.setInteger("Contubernium", this.contuberniumId);
		compound.setInteger("pilaLeft", this.pilaLeft);
		compound.setBoolean("holdingScutum", this.holdingScutum);
	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.contuberniumId = compound.getInteger("Contubernium");
		this.pilaLeft = compound.getInteger("pilaLeft");
		this.holdingScutum = compound.getBoolean("holdingScutum");
		if (!this.worldObj.isRemote) {
			this.getDataWatcher().updateObject(20, (byte)1);
		}
	}
}
