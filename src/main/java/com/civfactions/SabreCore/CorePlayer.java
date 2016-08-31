package com.civfactions.SabreCore;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.Documentable;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.util.Guard;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreApi.util.SabreUtil;

/**
 * Represents a player that has joined the server and may or may not be online
 * @author GFQ
 */
public class CorePlayer implements SabrePlayer, Documentable {
	
	private final SabreApi sabre;
	private final DataCollection<CorePlayer> db;
	
	// Unique ID of the player
	private final UUID uid;
	
	// The player's display name
	private String name;
	
	// The Bukkit player instance
	private Player bukkitPlayer;
	
	// Login and play time data
	private Date firstLogin;
	private Date lastLogin;
	private long playTime;
	private boolean banned;
	private String banMessage;
	
	// Whether the player is vanished or not
	private boolean vanished;
	
	// The player's bed location
	private Location bedLocation;
	
	private SabreDocument storedValues = new SabreDocument();	
	
	/**
	 * Creates a new SabrePlayer instance
	 * @param uid The player's ID
	 * @param name The player's display name
	 */
	public CorePlayer(final SabreApi sabre, final DataCollection<CorePlayer> db, final UUID uid, final String name) {
		Guard.ArgumentNotNull(sabre, "sabre");
		Guard.ArgumentNotNull(db, "db");
		Guard.ArgumentNotNull(uid, "uid");
		Guard.ArgumentNotNullOrEmpty(name, "name");
		
		this.sabre = sabre;
		this.db = db;
		this.uid = uid;
		this.name = name;
		this.firstLogin = sabre.getTimeNow();
		this.lastLogin = sabre.getTimeNow();
		this.playTime = 0;
		this.banned = false;
		this.banMessage = "";
		this.vanished = false;
	}
	
	/**
	 * Gets the Sabre API
	 * @return The Sabre API
	 */
	public SabreApi getSabreApi() {
		return sabre;
	}
	
	
	/**
	 * Gets the player ID
	 * @return The player ID
	 */
	@Override
	public UUID getUniqueId() {
		return uid;
	}
	
	
	/**
	 * Gets the player name
	 * @return The player name
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * Sets the player name
	 * @param name The player name
	 */
	public void setName(final String name) {
		Guard.ArgumentNotNullOrEmpty(name, "name");
		
		this.name = name;
		
		if (bukkitPlayer != null) {
			bukkitPlayer.setDisplayName(name);
			bukkitPlayer.setCustomName(name);
			bukkitPlayer.setPlayerListName(name);
		}
		db.updateField(this, "name", name);
	}
	
	
	/**
	 * Gets the Bukkit player instance
	 * @return The Bukkit player instance
	 */
	@Override
	public Player getBukkitPlayer() {
		return this.bukkitPlayer;
	}
	
	
	/**
	 * Sets the player instance
	 * @param bukkitPlayer The player instance
	 */
	@Override
	public void setBukkitPlayer(final Player bukkitPlayer) {
		this.bukkitPlayer = bukkitPlayer;
	}
	
	
	/**
	 * Gets whether the player is online
	 * @return true if the player is online
	 */
	@Override
	public boolean isOnline() {
		return bukkitPlayer != null && bukkitPlayer.isOnline();
	}
	
	
	/**
	 * Gets the first login time
	 * @return The first login time
	 */
	@Override
	public Date getFirstLogin() {
		return this.firstLogin;
	}
	
	
	/**
	 * Sets the first login time
	 * @param firstLogin The first login time
	 */
	public CorePlayer setFirstLogin(final Date firstLogin) {
		Guard.ArgumentNotNull(firstLogin, "firstLogin");
		
		this.firstLogin = firstLogin;
		return this;
	}
	
	
	/**
	 * Gets the last login time
	 * @return The first login time
	 */
	@Override
	public Date getLastLogin() {
		return this.lastLogin;
	}
	
	
	/**
	 * Sets the last login time
	 * @param lastLogin The first login time
	 */
	public CorePlayer setLastLogin(final Date lastLogin) {
		Guard.ArgumentNotNull(lastLogin, "lastLogin");
		
		this.lastLogin = lastLogin;
		return this;
	}
	
	
	/**
	 * Gets the number of days since last login
	 * @return The number of days since last login
	 */
	@Override
	public int getDaysSinceLastLogin() {
		Date now = sabre.getTimeNow();
		long timeDiff = now.getTime() - lastLogin.getTime();
		long diffDays = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
		return (int)diffDays;
	}
	
	
	/**
	 * Gets the total play time
	 * @return The total play time
	 */
	@Override
	public long getPlaytime() {
		return this.playTime;
	}
	
	
	/**
	 * Sets the total play time
	 * @param lastLogin The total play time
	 */
	public CorePlayer setPlaytime(final long playTime) {
		this.playTime = playTime;
		return this;
	}
	
	
	/**
	 * Adds to the total play time
	 * @param lastLogin The total play time
	 */
	public void addPlayTime(final long playTime) {
		this.playTime += playTime;
	}
	
	
	/**
	 * Gets the ban status
	 * @param banned Whether the player is banned
	 */
	@Override
	public boolean getBanned() {
		return this.banned;
	}

	
	/**
	 * Sets the ban status
	 * @return banned true if the player is banned
	 */
	public CorePlayer setBanned(final boolean banned, final String reason) {
		Guard.ArgumentNotNull(banMessage, "banMessage");
		
		this.banned = banned;
		this.banMessage = reason;
		
		db.updateField(this, "banned", true);
		db.updateField(this, "ban_message", banMessage);
		
		if (banned && isOnline()) {
			String fullBanMessage = String.format("%s\n%s", Lang.youAreBanned, reason);
			bukkitPlayer.kickPlayer(fullBanMessage);
		}
		return this;
	}
	
	
	/**
	 * Gets the ban message
	 * @param The ban message
	 */
	@Override
	public String getBanMessage() {
		return this.banMessage;
	}
	
	/**
	 * Sends a message to an online player
	 * @param str
	 * @param args
	 */
	@Override
	public void msg(final String str, final Object... args) {
		if (str == null || str == "") {
			return; // Silently ignore null or empty strings
		}
		
		String msg = sabre.formatText(str, args);
		if (this.isOnline()) {
			this.getBukkitPlayer().sendMessage(msg);
		}
	}
	
	
	/**
	 * Gets the distance from another player
	 * @param p The player to check
	 * @return The distance the players are away from each other
	 */
	@Override
	public int getDistanceFrom(final SabrePlayer other) {
		Guard.ArgumentNotNull(other, "other");
		
		if (!isOnline() || !other.isOnline()) {
			return -1;
		}
		
		Location myLocation = bukkitPlayer.getLocation();
		Location otherLocation = other.getBukkitPlayer().getLocation();
		int dx = otherLocation.getBlockX() - myLocation.getBlockX();
		int dz = otherLocation.getBlockZ() - myLocation.getBlockZ();
		
		return (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
	}
	
	
	/**
	 * Gets the admin vanished status
	 * @return true if the player is vanished
	 */
	@Override
	public boolean getVanished() {
		return this.vanished;
	}

	
	/**
	 * Sets the vanished status
	 * @param vanished The new vanished state
	 */
	@Override
	public CorePlayer setVanished(final boolean vanished) {
		this.vanished = vanished;
		return this;
	}
	
	
	/**
	 * Gets the player bed location
	 * @return the bed location
	 */
	@Override
	public Location getBedLocation() {
		return this.bedLocation;
	}

	
	/**
	 * Gets the bed location
	 * @param bedLocation The bed location
	 */
	public CorePlayer setBedLocation(final Location bedLocation) {
		this.bedLocation = bedLocation;
		return this;
	}
	
	
	/**
	 * Gets whether the player has admin permissions
	 */
	@Override
	public boolean isAdmin() {
		return isOnline() && bukkitPlayer.hasPermission(Permission.ADMIN_NODE);
	}
	
	/**
	 * Teleports the player to a location
	 * @param l The location
	 * @return True if successful
	 */
	@Override
	public boolean teleport(final Location location) {
		Guard.ArgumentNotNull(location, "location");
		
		if (isOnline()) {
			return bukkitPlayer.teleport(location);
		}
		return false;
	}
	
	/**
	 * Sends a player to the ground at a specified location
	 * @param player The player to move
	 * @return The final landing location
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Location teleportToGround(final Location location) {
		Location l = bukkitPlayer.getLocation();
		
		l.getChunk().load();
		Block block = l.getBlock();

		World world = l.getWorld();

		for(int y = 0 ; y <= l.getBlockY() + 2; y++){
			block = world.getBlockAt(l.getBlockX(), y, l.getBlockZ());
			bukkitPlayer.sendBlockChange(block.getLocation(), block.getType(), block.getData());
		}
		
		teleport(block.getLocation());
		return block.getLocation();
	}

	@Override
	public <T> T getValue(final String key) {
		return storedValues.getRaw(key);
	}

	@Override
	public <T> void setValue(final String key, final T value, final boolean persist) {
		storedValues.setRaw(key, value);
		
		if (persist) {
			db.updateField(this, key, value);
		}
	}

	@Override
	public boolean containsValue(String key) {
		return storedValues.containsField(key);
	}
	
	/**
	 * Gets the player ID
	 * @return The player ID
	 */
	@Override
	public String getDocumentKey() {
		return uid.toString();
	}

	@Override
	public SabreDocument getDocument() {
		SabreDocument doc = new SabreDocument()
				.append("_id", getUniqueId().toString())
				.append("name", getName())
				.append("first_login", getFirstLogin())
				.append("last_login", getLastLogin())
				.append("play_time", getPlaytime());
		
		return doc;
	}

	@Override
	public CorePlayer loadDocument(final SabreDocument doc) {
		firstLogin = doc.getDate("first_login", firstLogin);
		lastLogin = doc.getDate("last_login", lastLogin);
		playTime = doc.getLong("play_time", 0);
		banned = doc.getBoolean("banned", false);
		banMessage = doc.getString("ban_message", "");
		
		if (doc.containsField("bed")) {
			bedLocation = SabreUtil.deserializeLocation(doc.getDocument("bed"));
		}
		
		// The rest of the values will be stored here
		storedValues = new SabreDocument(doc);
		return this;
	}
}
