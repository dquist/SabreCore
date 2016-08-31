package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import com.civfactions.SabreApi.SabreLogger;
import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.Documentable;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.util.Guard;

public class ChunkBlocks implements Documentable, Map<Location, CoreBlock> {

	private final SabreLogger logger;
	private final DataCollection<ChunkBlocks> data;
	private final BlockUpdate update;
	private final World world;
	private final int x;
	private final int z;
	
	private final HashMap<Location, CoreBlock> blocks = new HashMap<Location, CoreBlock>();
	
	public ChunkBlocks(final SabreLogger logger, final DataCollection<ChunkBlocks> data, final World world, final int x, final int z) {
		Guard.ArgumentNotNull(logger, "logger");
		Guard.ArgumentNotNull(data, "data");
		Guard.ArgumentNotNull(world, "world");

		this.logger = logger;
		this.data = data;
		this.update = new BlockUpdate(this, this.data);
		this.world = world;
		this.x = x;
		this.z = z;
	}
	
	public World getWorld() {
		return world;
	}
	

	@Override
	public String getDocumentKey() {
		return formatChunkString(world, x, z);
	}

	@Override
	public SabreDocument getDocument() {
		return new SabreDocument()
				.append("_id", getDocumentKey())
				.append("world", world.getName())
				.append("x", x)
				.append("z", z);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ChunkBlocks loadDocument(SabreDocument doc) {
		
		if (doc.containsField("blocks")) {
			Map<String, Object> docBocks = (Map<String, Object>)doc.get("blocks");
			
			for(Entry<String, Object> e : docBocks.entrySet()) {
				try {
					SabreDocument blockDoc = new SabreDocument((Map<String, Object>)e.getValue());
					
					CoreBlock b = new CoreBlock(update, CoreBlock.getBlockFromKeyString(world, e.getKey())).loadDocument(blockDoc);
					blocks.put(b.getLocation(), b);
				} catch (Exception ex) {
					logger.log(Level.WARNING, "Failed to read block record %s", e.getValue().toString());
					ex.printStackTrace();
				}
			}
		}
		return this;
	}
	
	public static String formatChunkString(World w, int x, int z) {
		return String.format("%s,%d,%d", w.getName(), x, z);
	}
	
	public static String formatChunkString(Chunk c) {
		return formatChunkString(c.getWorld(), c.getX(), c.getZ());
	}


	@Override
	public int size() {
		return blocks.size();
	}


	@Override
	public boolean isEmpty() {
		return blocks.isEmpty();
	}


	@Override
	public boolean containsKey(Object key) {
		return blocks.containsKey(key);
	}


	@Override
	public boolean containsValue(Object value) {
		return blocks.containsValue(value);
	}


	@Override
	public CoreBlock get(Object key) {
		return blocks.get(key);
	}


	@Override
	public CoreBlock put(Location key, CoreBlock value) {
		return blocks.put(key, value);
	}


	@Override
	public CoreBlock remove(Object key) {
		return blocks.remove(key);
	}


	@Override
	public void putAll(Map<? extends Location, ? extends CoreBlock> m) {
		blocks.putAll(m);
	}


	@Override
	public void clear() {
		blocks.clear();
	}


	@Override
	public Set<Location> keySet() {
		return blocks.keySet();
	}


	@Override
	public Collection<CoreBlock> values() {
		return blocks.values();
	}


	@Override
	public Set<java.util.Map.Entry<Location, CoreBlock>> entrySet() {
		return blocks.entrySet();
	}
}
