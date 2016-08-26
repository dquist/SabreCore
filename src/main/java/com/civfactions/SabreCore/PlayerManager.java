package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.data.SabreObjectFactory;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.util.Guard;

/**
 * Class for managing all the player records
 * @author Gordon
 */
public class PlayerManager implements SabreObjectFactory<CorePlayer> {
	
	private final SabreApi sabre;
	private final DataStorage db;
	
	private DataCollection<CorePlayer> data;
	
	private final HashMap<UUID, CorePlayer> players;
	private final HashMap<UUID, CorePlayer> onlinePlayers;
	
	/**
	 * Creates a new PlayerManager instance 
	 */
	public PlayerManager(final SabreApi sabre, final DataStorage db) {
		Guard.ArgumentNotNull(sabre, "sabre");
		Guard.ArgumentNotNull(db, "db");
		
		this.sabre = sabre;
		this.db = db;
		
		this.players = new HashMap<UUID, CorePlayer>();
		this.onlinePlayers = new HashMap<UUID, CorePlayer>();
	}
	
	/**
	 * Loads all the player data from the database
	 */
	public void load() {
		this.data = db.getDataCollection("players", this);
		this.players.clear();

		Collection<CorePlayer> players = data.readAll();
		for (CorePlayer p : players) {
			this.players.put(p.getUniqueId(), p);
		}
		
		sabre.log("Loaded %d unique player records", players.size());
	}
	
	
	/**
	 * Removes a player from all records
	 * @param player The player to remove
	 */
	public void removePlayer(SabrePlayer player) {
		Guard.ArgumentNotNull(player, "player");
		assertValidPlayer(player);
		
		onlinePlayers.remove(player.getUniqueId());
		CorePlayer removed = players.remove(player.getUniqueId());
		data.remove(removed);
		sabre.log("Removed player: Name=%s, ID=%s", player.getName(), player.getUniqueId().toString());
	}
	
	
	/**
	 * Gets a SabrePlayer instance by ID
	 * @param id The ID of the player
	 * @return The player instance if it exists
	 */
	public SabrePlayer getPlayerById(final UUID uid) {
		Guard.ArgumentNotNull(uid, "uid");
		
		// Check online players first
		CorePlayer p = onlinePlayers.get(uid);
		if (p == null) {
			p = players.get(uid);
		}
		return p;
	}
	
	
	/**
	 * Gets a SabrePlayer instance by name
	 * @param id The name of the player
	 * @return The player instance if it exists
	 */
	public SabrePlayer getPlayerByName(final String name) {
		Guard.ArgumentNotNullOrEmpty(name, "name");
		
		// Check online players first
		for (CorePlayer p : onlinePlayers.values()) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		
		// Then check all players
		for (CorePlayer p : players.values()) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Creates a new player instance
	 * @param player The bukkit player
	 * @return The new SabrePlayer instance
	 */
	public SabrePlayer createNewPlayer(final Player player) {
		Guard.ArgumentNotNull(player, "player");
		
		String originalName = player.getName();
		String name = originalName;
		SabrePlayer sp = getPlayerByName(originalName);
		
		// If there is a conflict, add numbers to the end to get a name
		// that hasn't been used before
		Integer i = 0;
		while (sp != null) {
			name = originalName + i.toString();
			sp = getPlayerByName(name);
		}
		
		// Now we should have a unique name for the new player
		CorePlayer sPlayer = new CorePlayer(sabre, data, player.getUniqueId(), name);
		players.put(sPlayer.getUniqueId(), sPlayer);
		data.insert(sPlayer);
		
		sPlayer.setBukkitPlayer(player);
		sPlayer.setFirstLogin(new Date());
		sPlayer.setName(name);
		sabre.log("Created new player %s with ID %s", name, sPlayer.getUniqueId());
		return sPlayer;
	}
	
	/**
	 * Validates that a player instance is registered in the manager
	 * @param p The player to validate
	 * @return The matching SabrePlayer instance
	 */
	private CorePlayer assertValidPlayer(final SabrePlayer p) {
		UUID uid = p.getUniqueId();
		
		// Check online players first
		CorePlayer player = onlinePlayers.get(uid);
		
		// Then check all players
		if (player == null) {
			player = players.get(uid);
		}
		
		if (player == null) {
			throw new RuntimeException(String.format("Tried to access invalid player '%s' with ID %s", p.getName(), p.getUniqueId()));
		}
		
		return player;
	}

	/**
	 * Gets the collection of all players who have ever logged in
	 * @return The player list
	 */
	public Collection<SabrePlayer> getAllPlayers() {
		Set<SabrePlayer> players = this.players.values().stream().map(p -> (SabrePlayer)p).collect(Collectors.toSet());
		return Collections.unmodifiableSet(players);
	}


	/**
	 * Gets the collection of players who are online
	 * @return The player list
	 */
	public Collection<SabrePlayer> getOnlinePlayers() {
		Set<SabrePlayer> onlinePlayers = this.onlinePlayers.values().stream().map(p -> (SabrePlayer)p).collect(Collectors.toSet());
		return Collections.unmodifiableSet(onlinePlayers);
	}

	/**
	 * Factory method for creating a new player instance
	 * @param values The data values
	 */
	@Override
	public CorePlayer createInstance(final SabreDocument doc) {
		UUID uid = UUID.fromString(doc.get("_id").toString());
		String name = doc.get("name").toString();
		
		return new CorePlayer(sabre, data, uid, name).loadDocument(doc);
	}
}
