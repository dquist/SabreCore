package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.civfactions.SabreApi.SabreBlock;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.util.Guard;

public class CoreBlock implements SabreBlock {
	
	private final BlockUpdate update;
	private final Block b;
	
	private SabreDocument storedValues = new SabreDocument();
	
	CoreBlock(final BlockUpdate update, final Block b) {
		Guard.ArgumentNotNull(update, "update");
		Guard.ArgumentNotNull(b, "b");
		
		this.update = update;
		this.b = b;
	}
	
	CoreBlock(final BlockUpdate update, final Location l) {
		this(update, l.getBlock());
	}

	@Override
	public boolean breakNaturally() {
		return b.breakNaturally();
	}

	@Override
	public boolean breakNaturally(ItemStack arg0) {
		return b.breakNaturally(arg0);
	}

	@Override
	public Biome getBiome() {
		return b.getBiome();
	}

	@Override
	public int getBlockPower() {
		return b.getBlockPower();
	}

	@Override
	public int getBlockPower(BlockFace arg0) {
		return b.getBlockPower(arg0);
	}

	@Override
	public Chunk getChunk() {
		return b.getChunk();
	}

	@SuppressWarnings("deprecation")
	@Override
	public byte getData() {
		return b.getData();
	}

	@Override
	public Collection<ItemStack> getDrops() {
		return b.getDrops();
	}

	@Override
	public Collection<ItemStack> getDrops(ItemStack arg0) {
		return b.getDrops(arg0);
	}

	@Override
	public BlockFace getFace(Block arg0) {
		return b.getFace(arg0);
	}

	@Override
	public double getHumidity() {
		return b.getHumidity();
	}

	@Override
	public byte getLightFromBlocks() {
		return b.getLightFromBlocks();
	}

	@Override
	public byte getLightFromSky() {
		return b.getLightFromSky();
	}

	@Override
	public byte getLightLevel() {
		return b.getLightLevel();
	}

	@Override
	public Location getLocation() {
		return b.getLocation();
	}

	@Override
	public Location getLocation(Location arg0) {
		return b.getLocation(arg0);
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		return b.getPistonMoveReaction();
	}

	@Override
	public Block getRelative(BlockFace arg0) {
		return b.getRelative(arg0);
	}

	@Override
	public Block getRelative(BlockFace arg0, int arg1) {
		return b.getRelative(arg0, arg1);
	}

	@Override
	public Block getRelative(int arg0, int arg1, int arg2) {
		return b.getRelative(arg0, arg1, arg2);
	}

	@Override
	public BlockState getState() {
		return b.getState();
	}

	@Override
	public double getTemperature() {
		return b.getTemperature();
	}

	@Override
	public Material getType() {
		return b.getType();
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getTypeId() {
		return b.getTypeId();
	}

	@Override
	public World getWorld() {
		return b.getWorld();
	}

	@Override
	public int getX() {
		return b.getX();
	}

	@Override
	public int getY() {
		return b.getY();
	}

	@Override
	public int getZ() {
		return b.getZ();
	}

	@Override
	public boolean isBlockFaceIndirectlyPowered(BlockFace arg0) {
		return b.isBlockFaceIndirectlyPowered(arg0);
	}

	@Override
	public boolean isBlockFacePowered(BlockFace arg0) {
		return b.isBlockFacePowered(arg0);
	}

	@Override
	public boolean isBlockIndirectlyPowered() {
		return b.isBlockIndirectlyPowered();
	}

	@Override
	public boolean isBlockPowered() {
		return b.isBlockPowered();
	}

	@Override
	public boolean isEmpty() {
		return b.isEmpty();
	}

	@Override
	public boolean isLiquid() {
		return b.isLiquid();
	}

	@Override
	public void setBiome(Biome arg0) {
		b.setBiome(arg0);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setData(byte arg0) {
		b.setData(arg0);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setData(byte arg0, boolean arg1) {
		b.setData(arg0, arg1);
		
	}

	@Override
	public void setType(Material arg0) {
		b.setType(arg0);
	}

	@Override
	public void setType(Material arg0, boolean arg1) {
		b.setType(arg0, arg1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean setTypeId(int arg0) {
		return b.setTypeId(arg0);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean setTypeId(int arg0, boolean arg1) {
		return b.setTypeId(arg0, arg1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean setTypeIdAndData(int arg0, byte arg1, boolean arg2) {
		return b.setTypeIdAndData(arg0, arg1, arg2);
	}

	@Override
	public List<MetadataValue> getMetadata(String arg0) {
		return b.getMetadata(arg0);
	}

	@Override
	public boolean hasMetadata(String arg0) {
		return b.hasMetadata(arg0);
	}

	@Override
	public void removeMetadata(String arg0, Plugin arg1) {
		b.removeMetadata(arg0, arg1);
	}

	@Override
	public void setMetadata(String arg0, MetadataValue arg1) {
		b.setMetadata(arg0, arg1);
	}

	@Override
	public String getDocumentKey() {
		Location l = b.getLocation();
		return String.format("%d,%d,%d", l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}

	@Override
	public SabreDocument getDocument() {
		return storedValues;
	}

	@Override
	public CoreBlock loadDocument(SabreDocument doc) {
		storedValues = new SabreDocument(doc);
		return this;
	}

	@Override
	public <T> T getValue(String key) {
		return storedValues.getRaw(key);
	}

	@Override
	public <T> void setValue(String key, T value, boolean persist) {
		storedValues.setRaw(key, value);
		
		if (persist) {
			update.updateField(this, key, value);
		}
	}

	@Override
	public boolean containsValue(String key) {
		return storedValues.containsField(key);
	}
	
	public static Block getBlockFromKeyString(World w, String key) {
		String[] coords = key.split(",");
		int x = Integer.parseInt(coords[0]);
		int y = Integer.parseInt(coords[1]);
		int z = Integer.parseInt(coords[2]);
		return w.getBlockAt(x, y, z);
	}
}
