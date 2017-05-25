package mrriegel.storagenetwork.blocks;

import java.util.List;
import java.util.Random;

import mrriegel.storagenetwork.CreativeTab;
import mrriegel.storagenetwork.StorageNetwork;
import mrriegel.storagenetwork.config.ConfigHandler;
import mrriegel.storagenetwork.handler.GuiHandler;
import mrriegel.storagenetwork.init.ModItems;
import mrriegel.storagenetwork.tile.TileItemBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import com.google.common.collect.Lists;

public class BlockItemBox extends BlockConnectable {

	public BlockItemBox() {
		super(Material.IRON);
		this.setHardness(2.0F);
		this.setCreativeTab(CreativeTab.tab1);
		this.setRegistryName("itemBox");
		this.setUnlocalizedName(getRegistryName().toString());
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileItemBox();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileItemBox tile = (TileItemBox) worldIn.getTileEntity(pos);
		if (stack.getTagCompound() != null)
			tile.readInventory(stack.getTagCompound());
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode) {
			this.dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		try {
			ItemStack stack = new ItemStack(getItemDropped(state, new Random(), fortune));
			NBTTagCompound x = new NBTTagCompound();
			((TileItemBox) world.getTileEntity(pos)).writeInventory(x);
			stack.setTagCompound(x);
			return Lists.newArrayList(stack);
		} catch (Exception e) {
			return super.getDrops(world, pos, state, fortune);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,  EnumFacing side, float hitX, float hitY, float hitZ) {
	  ItemStack heldItem = playerIn.getHeldItem(hand);
	  if (/* tile.getMaster() == null || */(heldItem != null && (heldItem.getItem() == ModItems.coverstick || heldItem.getItem() == ModItems.duplicator)))
			return false;
		if (!(worldIn.getTileEntity(pos) instanceof TileItemBox))
			return false;
		if (worldIn.getTileEntity(pos) instanceof TileItemBox) {
			TileItemBox tile = (TileItemBox) worldIn.getTileEntity(pos);
			if (worldIn.isRemote)
				return true;
			playerIn.openGui(StorageNetwork.instance, GuiHandler.CABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;

	}

	public static class Item extends ItemBlock {

		public Item(Block block) {
			super(block);
		}

		@Override
		public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
			super.addInformation(stack, playerIn, tooltip, advanced);
			tooltip.add(I18n.format("tooltip.storagenetwork.itembox"));
			tooltip.add(I18n.format("tooltip.storagenetwork.networkNeeded"));
			if (stack.getTagCompound() == null)
				return;
			tooltip.add("Slots: " + stack.getTagCompound().getTagList("box", Constants.NBT.TAG_COMPOUND).tagCount() + "/" + ConfigHandler.itemBoxCapacity);
		}

	}

}
