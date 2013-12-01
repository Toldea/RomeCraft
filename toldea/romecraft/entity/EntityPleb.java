package toldea.romecraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import toldea.romecraft.RomeCraft;
import toldea.romecraft.entity.ai.EntityAIBlacksmithing;
import toldea.romecraft.entity.ai.EntityAIPlebMate;
import toldea.romecraft.managers.GuiManager;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.managers.TickManager;
import toldea.romecraft.romanvillage.BlacksmithOrders;
import toldea.romecraft.romanvillage.RomanVillage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityPleb extends EntityAgeable implements INpc, IEntityAdditionalSpawnData {
	public enum PLEB_PROFESSION {
		NONE(0), BLACKSMITH(1);
		private final int value;

		private PLEB_PROFESSION(int value) {
			this.value = value;
		}

		public final int getIntValue() {
			return value;
		}

		public static final PLEB_PROFESSION professionForInt(int value) {
			switch (value) {
			case 0:
				return PLEB_PROFESSION.NONE;
			case 1:
				return PLEB_PROFESSION.BLACKSMITH;
			default:
				return PLEB_PROFESSION.NONE;
			}
		}
	}

	public enum PLEB_EQUIPMENT {
		HAMMER
	}

	private int randomTickDivider;
	private boolean isMating;
	private boolean isPlaying;
	RomanVillage villageObj;

	private boolean field_82190_bM;
	private float field_82191_bN;
	
	private BlacksmithOrders blacksmithOrders = null;

	public EntityPleb(World par1World) {
		this(par1World, PLEB_PROFESSION.NONE);
	}

	public EntityPleb(World par1World, PLEB_PROFESSION profession) {
		super(par1World);

		this.setSize(0.6F, 1.8F);

		this.setProfession(profession);

		this.getNavigator().setBreakDoors(true);
		this.getNavigator().setAvoidsWater(true);

		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));

		// this.tasks.addTask(1, new EntityAITradePlayer(this));
		// this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));

		this.tasks.addTask(2, new EntityAIBlacksmithing(this));

		this.tasks.addTask(4, new EntityAIMoveIndoors(this));
		this.tasks.addTask(5, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(7, new EntityAIMoveTowardsRestriction(this, 0.6D));

		this.tasks.addTask(8, new EntityAIPlebMate(this));

		// this.tasks.addTask(9, new EntityAIFollowGolem(this));
		// this.tasks.addTask(10, new EntityAIPlay(this, 0.32D));

		this.tasks.addTask(11, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(11, new EntityAIWatchClosest2(this, EntityPleb.class, 5.0F, 0.02F));
		this.tasks.addTask(11, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(12, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.5D);
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	public void equipItem(PLEB_EQUIPMENT equipment) {
		switch (equipment) {
		case HAMMER:
			equipItemToSlot(ItemManager.itemHammer, 0);
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

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	protected void updateAITick() {
		if (--this.randomTickDivider <= 0) {
			TickManager.romanVillageCollection.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY),
					MathHelper.floor_double(this.posZ));
			this.randomTickDivider = 70 + this.rand.nextInt(50);
			this.villageObj = TickManager.romanVillageCollection.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY),
					MathHelper.floor_double(this.posZ), 32);

			if (this.villageObj == null) {
				this.detachHome();
			} else {
				ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
				this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int) ((float) this.villageObj.getVillageRadius() * 0.6F));

				if (this.field_82190_bM) {
					this.field_82190_bM = false;
					this.villageObj.func_82683_b(5);
				}
			}
		}

		super.updateAITick();
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();

		if (itemstack == null) {
			switch (this.getProfession()) {
			case BLACKSMITH:
				player.openGui(RomeCraft.instance, GuiManager.blacksmithGuiId, worldObj, this.entityId, -1, -1);
				return true;
			default:
				break;
			}
		} else if (this.isEntityAlive() && !this.isChild()) {
			int id = itemstack.itemID;
			boolean usedItem = false;

			if (id == ItemManager.itemLegionaryEquipment.itemID) {
				if (!this.worldObj.isRemote) {
					convertToLegionary();
				}
			} else if (id == ItemManager.itemBlacksmithEquipment.itemID) {
				System.out.println("Convertin pleb on side: " + worldObj.isRemote);
				this.convertToProfession(PLEB_PROFESSION.BLACKSMITH);
			}

			if (usedItem) {
				if (!player.capabilities.isCreativeMode) {
					--itemstack.stackSize;
				}
				if (itemstack.stackSize <= 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
				}
				return true;
			}
		}
		return super.interact(player);
	}

	private void convertToLegionary() {
		EntityLegionary entityLegionary = new EntityLegionary(this.worldObj);
		entityLegionary.copyLocationAndAnglesFrom(this);
		entityLegionary.onSpawnWithEgg((EntityLivingData) null);

		this.worldObj.removeEntity(this);
		this.worldObj.spawnEntityInWorld(entityLegionary);
		this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1017, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
	}

	private void convertToProfession(PLEB_PROFESSION profession) {
		if (this.setProfession(profession)) {
			this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1017, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
			switch (profession) {
			case BLACKSMITH:
				blacksmithOrders = new BlacksmithOrders();
				// this.equipItem(PLEB_EQUIPMENT.HAMMER); // TODO: Fix blacksmithing logic so this doesn't screw things up.
				break;
			default:
				break;
			}
		}
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Integer.valueOf(0));
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return false;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.villager.idle";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.villager.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.villager.death";
	}

	public boolean setProfession(PLEB_PROFESSION par1) {
		if (getProfession() != par1) {
			this.dataWatcher.updateObject(16, Integer.valueOf(par1.getIntValue()));
			return true;
		} else {
			return false;
		}
	}

	public PLEB_PROFESSION getProfession() {
		return PLEB_PROFESSION.professionForInt(this.dataWatcher.getWatchableObjectInt(16));
	}

	public boolean isMating() {
		return this.isMating;
	}

	public void setMating(boolean par1) {
		this.isMating = par1;
	}

	public void setPlaying(boolean par1) {
		this.isPlaying = par1;
	}

	public boolean isPlaying() {
		return this.isPlaying;
	}

	public void setRevengeTarget(EntityLivingBase par1EntityLivingBase) {
		super.setRevengeTarget(par1EntityLivingBase);
		/*
		 * if (this.villageObj != null && par1EntityLivingBase != null) { this.villageObj.addOrRenewAgressor(par1EntityLivingBase);
		 * 
		 * if (par1EntityLivingBase instanceof EntityPlayer) { byte b0 = -1;
		 * 
		 * if (this.isChild()) { b0 = -3; }
		 * 
		 * this.villageObj.setReputationForPlayer(((EntityPlayer)par1EntityLivingBase).getCommandSenderName(), b0);
		 * 
		 * if (this.isEntityAlive()) { this.worldObj.setEntityState(this, (byte)13); } } }
		 */
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource) {
		if (this.villageObj != null) {
			Entity entity = par1DamageSource.getEntity();

			if (entity != null) {
				if (entity instanceof EntityPlayer) {
					this.villageObj.setReputationForPlayer(((EntityPlayer) entity).getCommandSenderName(), -2);
				} else if (entity instanceof IMob) {
					this.villageObj.endMatingSeason();
				}
			} else if (entity == null) {
				EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

				if (entityplayer != null) {
					this.villageObj.endMatingSeason();
				}
			}
		}

		super.onDeath(par1DamageSource);
	}

	public void func_110297_a_(ItemStack par1ItemStack) {
		if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
			this.livingSoundTime = -this.getTalkInterval();

			if (par1ItemStack != null) {
				this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
			} else {
				this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte par1) {
		if (par1 == 12) {
			this.generateRandomParticles("heart");
		} else if (par1 == 13) {
			this.generateRandomParticles("angryVillager");
		} else if (par1 == 14) {
			this.generateRandomParticles("happyVillager");
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData) {
		par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
		// VillagerRegistry.applyRandomTrade(this, worldObj.rand);
		return par1EntityLivingData;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * par1 is the particleName
	 */
	private void generateRandomParticles(String par1Str) {
		for (int i = 0; i < 5; ++i) {
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(par1Str, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 1.0D
					+ (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
					d0, d1, d2);
		}
	}

	public void func_82187_q() {
		this.field_82190_bM = true;
	}

	public EntityPleb createPlebChild(EntityAgeable par1EntityAgeable) {
		EntityPleb entityPleb = new EntityPleb(this.worldObj);
		entityPleb.onSpawnWithEgg((EntityLivingData) null);
		return entityPleb;
	}

	public boolean allowLeashing() {
		return false;
	}

	public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
		return createPlebChild(par1EntityAgeable);
	}
	
	public BlacksmithOrders getBlacksmithOrders() {
		return blacksmithOrders;
	}
	
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Profession", this.getProfession().getIntValue());
		
		if (this.getProfession() == PLEB_PROFESSION.BLACKSMITH) {
			this.blacksmithOrders.writeOrdersToNBT(par1NBTTagCompound);
		}
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setProfession(PLEB_PROFESSION.professionForInt(par1NBTTagCompound.getInteger("Profession")));
		
		if (this.getProfession() == PLEB_PROFESSION.BLACKSMITH) {
			this.blacksmithOrders = new BlacksmithOrders();
			this.blacksmithOrders.readOrdersFromNBT(par1NBTTagCompound);
		}
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		if (this.getProfession() == PLEB_PROFESSION.BLACKSMITH && blacksmithOrders != null) {
			int[] quantities = blacksmithOrders.getAllQuantities();
			data.writeShort(quantities.length);
			for (int i = 0; i < quantities.length; i++) {
				data.writeShort(quantities[i]);
			}
		}
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		if (this.getProfession() == PLEB_PROFESSION.BLACKSMITH) {
			int length = data.readShort();
			int[] quantities = new int[length];
			for (int i = 0; i < length; i++) {
				quantities[i] = data.readShort();
			}
			this.blacksmithOrders = new BlacksmithOrders();
			blacksmithOrders.setQuantities(quantities);
		}
	}
}