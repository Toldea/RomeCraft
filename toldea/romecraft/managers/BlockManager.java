package toldea.romecraft.managers;

import toldea.romecraft.block.BlockBloomery;
import toldea.romecraft.block.BlockGag;
import toldea.romecraft.block.BlockMultiFurnaceCore;
import toldea.romecraft.block.BlockMultiFurnaceDummy;
import toldea.romecraft.block.BlockRomanVillageForum;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityGag;
import toldea.romecraft.tileentity.TileEntityMultiFurnaceCore;
import toldea.romecraft.tileentity.TileEntityMultiFurnaceDummy;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;

public class BlockManager {
	private static int nextBlockID = 500;

	public static Block romanVillageForum = null;

	public static Block multiFurnaceCore = null;
	public static Block multiFurnaceDummy = null;
	public static Block blockBloomery = null;
	public static Block blockGag = null;

	public static void registerBlocks() {

		// RomanVillageForum Block
		romanVillageForum = new BlockRomanVillageForum(nextBlockID++, Material.rock).setUnlocalizedName("romanVillageForum").setCreativeTab(
				CreativeTabsManager.tabRomeCraft);
		GameRegistry.registerBlock(romanVillageForum, "blockRomanVillageForum");
		LanguageRegistry.addName(romanVillageForum, "Forum");
		GameRegistry.registerTileEntity(TileEntityRomanVillageForum.class, "tileEntityRomanVillageForum");

		// Bloomery Block
		blockBloomery = new BlockBloomery(nextBlockID++, Material.clay).setHardness(0.5F).setStepSound(Block.soundGravelFootstep)
				.setUnlocalizedName("bloomery").setCreativeTab(CreativeTabsManager.tabRomeCraft).setTextureName("romecraft:bloomery");
		GameRegistry.registerBlock(blockBloomery, "bloomery");
		LanguageRegistry.addName(blockBloomery, "Bloomery Block");
		MinecraftForge.setBlockHarvestLevel(blockBloomery, "shovel", 0);
		GameRegistry.registerTileEntity(TileEntityBloomery.class, "tileEntityBloomery");
		// 'Gag' invisable multi block.
		blockGag = new BlockGag(nextBlockID++, Material.clay).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setUnlocalizedName("gag")
				.setTextureName("romecraft:bloomery");
		GameRegistry.registerBlock(blockGag, "gag");
		GameRegistry.registerTileEntity(TileEntityGag.class, "tileEntityGag");

		// MultiFurnaceCore
		multiFurnaceCore = new BlockMultiFurnaceCore(nextBlockID++, Material.rock).setUnlocalizedName("multiFurnaceCore");
		GameRegistry.registerBlock(multiFurnaceCore, "blockMultiFurnaceCore");
		LanguageRegistry.addName(multiFurnaceCore, "Multi-Furnace Core");
		GameRegistry.registerTileEntity(TileEntityMultiFurnaceCore.class, "tileEntityMultiFurnaceCore");
		// MultiFurnaceDummy
		multiFurnaceDummy = new BlockMultiFurnaceDummy(nextBlockID++).setUnlocalizedName("multiFurnaceDummy");
		GameRegistry.registerBlock(multiFurnaceDummy, "blockMultiFurnaceDummy");
		LanguageRegistry.addName(multiFurnaceDummy, "Multi-Furnace Dummy");
		GameRegistry.registerTileEntity(TileEntityMultiFurnaceDummy.class, "tileEntityMultiFurnaceDummy");

		// Register Crafting Recipes.
		registerCraftingRecipes();
	}

	private static void registerCraftingRecipes() {
		CraftingManager.getInstance().addRecipe(new ItemStack(multiFurnaceCore, 1), "XXX", "X X", "XXX", 'X', Block.brick);
	}
}
