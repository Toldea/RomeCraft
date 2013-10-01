package toldea.romecraft.managers;

import toldea.romecraft.block.BlockBloomery;
import toldea.romecraft.block.BlockGag;
import toldea.romecraft.block.BlockMultiFurnaceCore;
import toldea.romecraft.block.BlockMultiFurnaceDummy;
import toldea.romecraft.block.BlockRomanVillageForum;
import toldea.romecraft.block.RomeCraftBlock;
import toldea.romecraft.block.RomeCraftBlockStep;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityGag;
import toldea.romecraft.tileentity.TileEntityMultiFurnaceCore;
import toldea.romecraft.tileentity.TileEntityMultiFurnaceDummy;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockStep;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;

public class BlockManager {
	private static int nextBlockID = 500;

	public static Block blockRomanBricks = null;
	public static Block blockRomanBricksHalfSlab = null;
	public static Block blockRomanBricksDoubleSlab = null;

	public static Block romanVillageForum = null;

	public static Block multiFurnaceCore = null;
	public static Block multiFurnaceDummy = null;
	public static Block blockBloomery = null;
	public static Block blockGag = null;

	public static void registerBlocks() {
		/*
		 * blockRomanBricksHalfSlab = (BlockHalfSlab) (new RomeCraftBlockStep(nextBlockID++, false)).setHardness(2.0F).setResistance(10.0F)
		 * .setStepSound(Block.soundStoneFootstep).setUnlocalizedName("blockRomanBricksSlab").setTextureName("romecraft:romanbrick");
		 * GameRegistry.registerBlock(blockRomanBricksHalfSlab, "blockRomanBricksHalfSlab"); LanguageRegistry.addName(blockRomanBricksHalfSlab,
		 * "Roman Bricks Slab");
		 */

		// Roman Bricks
		blockRomanBricks = new RomeCraftBlock(nextBlockID++, Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("romanbrick").setTextureName("romecraft:romanbrick");
		GameRegistry.registerBlock(blockRomanBricks, "blockRomanBricks");
		LanguageRegistry.addName(blockRomanBricks, "Roman Bricks");

		blockRomanBricksHalfSlab = ((BlockStep) new RomeCraftBlockStep(nextBlockID++, false)).setHardness(2.0F).setResistance(10.0F)
				.setStepSound(Block.soundStoneFootstep).setUnlocalizedName("blockRomanBricksHalfSlab").setTextureName("romecraft:romanbrick");
		GameRegistry.registerBlock(blockRomanBricksHalfSlab, "blockRomanBricksHalfSlab");
		LanguageRegistry.addName(blockRomanBricksHalfSlab, "Roman Bricks Slab");

		blockRomanBricksDoubleSlab = ((BlockHalfSlab) new RomeCraftBlockStep(nextBlockID++, true)).setHardness(2.0F).setResistance(10.0F)
				.setStepSound(Block.soundStoneFootstep).setUnlocalizedName("blockRomanBricksDoubleSlab").setTextureName("romecraft:romanbrick");
		GameRegistry.registerBlock(blockRomanBricksDoubleSlab, "blockRomanBricksDoubleSlab");
		LanguageRegistry.addName(blockRomanBricksDoubleSlab, "Roman Bricks Double Slab");

		// RomanVillageForum Block
		romanVillageForum = new BlockRomanVillageForum(nextBlockID++, Material.rock).setUnlocalizedName("romanVillageForum");
		GameRegistry.registerBlock(romanVillageForum, "blockRomanVillageForum");
		LanguageRegistry.addName(romanVillageForum, "Forum");

		// Bloomery Block
		blockBloomery = new BlockBloomery(nextBlockID++, Material.clay).setHardness(0.5F).setStepSound(Block.soundGravelFootstep)
				.setUnlocalizedName("bloomery").setCreativeTab(CreativeTabsManager.tabRomeCraft).setTextureName("romecraft:bloomery");
		GameRegistry.registerBlock(blockBloomery, "bloomery");
		LanguageRegistry.addName(blockBloomery, "Bloomery Block");
		MinecraftForge.setBlockHarvestLevel(blockBloomery, "shovel", 0);

		// 'Gag' invisable multi block.
		blockGag = new BlockGag(nextBlockID++, Material.clay).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setUnlocalizedName("gag")
				.setTextureName("romecraft:bloomery");
		GameRegistry.registerBlock(blockGag, "gag");

		// MultiFurnaceCore
		multiFurnaceCore = new BlockMultiFurnaceCore(nextBlockID++, Material.rock).setUnlocalizedName("multiFurnaceCore");
		GameRegistry.registerBlock(multiFurnaceCore, "blockMultiFurnaceCore");
		LanguageRegistry.addName(multiFurnaceCore, "Multi-Furnace Core");

		// MultiFurnaceDummy
		multiFurnaceDummy = new BlockMultiFurnaceDummy(nextBlockID++).setUnlocalizedName("multiFurnaceDummy");
		GameRegistry.registerBlock(multiFurnaceDummy, "blockMultiFurnaceDummy");
		LanguageRegistry.addName(multiFurnaceDummy, "Multi-Furnace Dummy");

		// Register Crafting Recipes.
		registerCraftingRecipes();
	}

	private static void registerCraftingRecipes() {
		CraftingManager.getInstance().addRecipe(new ItemStack(multiFurnaceCore, 1), "XXX", "X X", "XXX", 'X', Block.brick);
	}
}