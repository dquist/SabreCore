package com.civfactions.SabreCore;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.DataStorage;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.data.SabreObjectFactory;
import com.civfactions.SabreApi.util.Guard;

/**
 * Class for managing all the block records
 * @author Gordon
 */
class BlockManager implements SabreObjectFactory<ChunkBlocks>, Listener {
	
	private final SabreApi sabre;
	private final DataStorage db;
	
	private DataCollection<ChunkBlocks> data;
	
	private HashMap<Chunk, ChunkBlocks> loadedChunks = new HashMap<Chunk, ChunkBlocks>();

	/**
	 * Creates a new BlockManager instance
	 * @param sabre The Sabre API
	 * @param db The database object
	 */
	BlockManager(final SabreApi sabre, final DataStorage db) {
		Guard.ArgumentNotNull(sabre, "sabre");
		Guard.ArgumentNotNull(db, "db");
		
		this.sabre = sabre;
		this.db = db;
	}
	
	
	public void load() {
		this.data = db.getDataCollection("chunks", this);
	}
	
	
	/**
	 * Gets a block record for a given location
	 * @param l The location to get
	 * @return The block instance if it exists
	 */
	public CoreBlock getBlockRecord(Location l) {
		Guard.ArgumentNotNull(l, "l");
		
		ChunkBlocks c = loadedChunks.get(l.getChunk());
		if (c == null) {
			return null;
		}
		return c.get(l);
	}
	
	
	/**
	 * Creates a new block record
	 * @param b The block record to add
	 */
	public void createBlockRecord(CoreBlock b) {
		Guard.ArgumentNotNull(b, "b");
		
		ChunkBlocks c = loadedChunks.get(b.getChunk());
		if (c == null) {
			c = new ChunkBlocks(sabre, data, b.getWorld(), b.getChunk().getX(), b.getChunk().getZ());
			data.insert(c);
		}

		c.put(b.getLocation(), b);
		data.updateField(c, "blocks." + b.getDocumentKey(), b);
	}


	@Override
	public ChunkBlocks createInstance(SabreDocument doc) {
		String worldName = doc.getString("world");
		int x = doc.getInteger("x");
		int z = doc.getInteger("z");
		
		return new ChunkBlocks(sabre, data, Bukkit.getWorld(worldName), x, z).loadDocument(doc);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkLoad(ChunkLoadEvent e) {
		ChunkBlocks c = data.readDocument("_id", ChunkBlocks.formatChunkString(e.getChunk()));
		if (c != null) {
			loadedChunks.put(e.getChunk(), c);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkLoad(ChunkUnloadEvent e) {
		loadedChunks.remove(e.getChunk());
	}
}
