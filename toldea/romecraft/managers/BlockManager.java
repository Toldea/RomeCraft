package toldea.romecraft.managers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import toldea.romecraft.block.BlockBloomery;
import toldea.romecraft.block.BlockGag;
import toldea.romecraft.block.RomeCraftMultiSidedBlock;
import toldea.romecraft.block.BlockMultiFurnaceCore;
import toldea.romecraft.block.BlockMultiFurnaceDummy;
import toldea.romecraft.block.BlockRomanBrickSlab;
import toldea.romecraft.block.BlockRomanVillageForum;
import toldea.romecraft.block.RomeCraftBlock;
import toldea.romecraft.block.RomeCraftBlockHalfSlab;
import toldea.romecraft.block.RomeCraftBlockStairs;
import toldea.romecraft.item.RomeCraftItemSlab;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockManager {
	private static int nextBlockID = 500;

	public static Block blockRomanBricks = null;
	public static RomeCraftBlockHalfSlab blockRomanBricksHalfSlab = null;
	public static RomeCraftBlockHalfSlab blockRomanBricksDoubleSlab = null;
	public static Block blockRomanBricksStairs = null;
	
	public static Block blockMarble = null;

	public static Block romanVillageForum = null;

	public static Block multiFurnaceCore = null;
	public static Block multiFurnaceDummy = null;
	public static Block blockBloomery = null;
	public static Block blockGag = null;

	public static void registerBlocks() {
		// Roman Bricks
		blockRomanBricks = new RomeCraftBlock(nextBlockID++, Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("blockRomanBricks").setTextureName("romecraft:romanbrick");
		GameRegistry.registerBlock(blockRomanBricks, "blockRomanBricks");
		LanguageRegistry.addName(blockRomanBricks, "Roman Bricks");

		blockRomanBricksHalfSlab = (RomeCraftBlockHalfSlab) new BlockRomanBrickSlab(nextBlockID++, false).setHardness(2.0F).setResistance(10.0F)
				.setStepSound(Block.soundStoneFootstep).setUnlocalizedName("blockRomanBricksSlab").setTextureName("romecraft:romanbrick");
		blockRomanBricksDoubleSlab = (RomeCraftBlockHalfSlab) new BlockRomanBrickSlab(nextBlockID++, true).setHardness(2.0F).setResistance(10.0F)
				.setStepSound(Block.soundStoneFootstep).setUnlocalizedName("blockRomanBricksSlab").setTextureName("romecraft:romanbrick");
		GameRegistry.registerBlock(blockRomanBricksHalfSlab, "blockRomanBricksHalfSlab");
		GameRegistry.registerBlock(blockRomanBricksDoubleSlab, "blockRomanBricksDoubleSlab");
		LanguageRegistry.instance().addStringLocalization(((BlockRomanBrickSlab)blockRomanBricksHalfSlab).getFullSlabName(0)+".name", "Roman Bricks Slab");
		LanguageRegistry.addName(blockRomanBricksDoubleSlab, "Roman Bricks Double Slab");
		Item.itemsList[blockRomanBricksHalfSlab.blockID] = (new RomeCraftItemSlab(blockRomanBricksHalfSlab.blockID - 256,
				(RomeCraftBlockHalfSlab) blockRomanBricksHalfSlab, (RomeCraftBlockHalfSlab) blockRomanBricksDoubleSlab, false));
		
		blockRomanBricksStairs = (new RomeCraftBlockStairs(nextBlockID++, blockRomanBricks, 0)).setUnlocalizedName("blockRomanBricksStairs");
		GameRegistry.registerBlock(blockRomanBricksStairs, "blockRomanBricksStairs");
		LanguageRegistry.addName(blockRomanBricksStairs, "Roman Brick Stairs");
		
		// Marble
		blockMarble = new RomeCraftMultiSidedBlock(nextBlockID++, Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("blockMarble").setTextureName("romecraft:marble_block");
		GameRegistry.registerBlock(blockMarble, "blockMarble");
		LanguageRegistry.addName(blockMarble, "Marble");
		
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