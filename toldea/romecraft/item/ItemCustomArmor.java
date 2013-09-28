package toldea.romecraft.item;

import toldea.romecraft.client.model.ModelLegionaryArmor;
import toldea.romecraft.managers.CreativeTabsManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemCustomArmor extends ItemArmor {
	private ModelBiped armorModel;
	ResourceLocation textureLoricaSegmentata = new ResourceLocation("romecraft", "textures/models/armor/loricasegmentata.png");

	public ItemCustomArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
		super(par1, par2EnumArmorMaterial, par3, par4);
		armorModel = new ModelLegionaryArmor();
		this.setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		return armorModel;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return textureLoricaSegmentata.toString();
	}
}
