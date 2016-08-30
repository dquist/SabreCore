package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import com.civfactions.SabreApi.BlockPermission;
import com.civfactions.SabreApi.PlayerSpawner;
import com.civfactions.SabreApi.PlayerVanisher;
import com.civfactions.SabreApi.SabreCommand;
import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.SabrePlugin;
import com.civfactions.SabreApi.data.DataAccess;
import com.civfactions.SabreApi.data.SabreConfig;
import com.civfactions.SabreApi.util.TextFormatter;
import com.civfactions.SabreCore.cmd.CmdAutoHelp;
import com.civfactions.SabreCore.cmd.CmdBan;
import com.civfactions.SabreCore.cmd.CmdFly;
import com.civfactions.SabreCore.cmd.CmdGamemode;
import com.civfactions.SabreCore.cmd.CmdMore;
import com.civfactions.SabreCore.cmd.CmdSabreRespawn;
import com.civfactions.SabreCore.cmd.CmdSabre;
import com.civfactions.SabreCore.cmd.CmdSetSpawn;
import com.civfactions.SabreCore.cmd.CmdUnban;
import com.civfactions.SabreCore.cmd.CmdVanish;
import com.civfactions.SabreCore.data.CoreConfiguration;
import com.civfactions.SabreCore.data.MongoStorage;
import com.civfactions.SabreCore.util.TextUtil;

/**
 * This class is the provides the core Sabre classes.
 *
 */
public class SabreCorePlugin extends SabrePlugin {
	
	private final DataStorage storage = new MongoStorage(this);
	private final PlayerManager pm = new PlayerManager(this, storage);
	private final CorePlayerSpawner spawner = new CorePlayerSpawner(this);
	private final CoreVanisher vanisher = new CoreVanisher();
	private final CmdAutoHelp autoHelp = new CmdAutoHelp(this);
	private final TextUtil textUtil = new TextUtil();
	
	private final StaticBlockPermission defaultBlockPerms = new StaticBlockPermission(true, true);
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		// Register core commands
		registerCommand(new CmdSabre(this));
		registerCommand(new CmdBan(this));
		registerCommand(new CmdFly(this));
		registerCommand(new CmdGamemode(this));
		registerCommand(new CmdMore(this));
		registerCommand(new CmdSabreRespawn(this));
		registerCommand(new CmdSetSpawn(this));
		registerCommand(new CmdUnban(this));
		registerCommand(new CmdVanish(this));
		
		// Register configuration objects
		registerConfigObject(storage);
		registerConfigObject(spawner);
		registerConfigObject(textUtil);
		
		// Loads data into all configuration objects
		readConfiguration();
		
		if (storage.connect()) {
			pm.load();
		}
		
		super.postEnable();
	}
	
	@Override
	public void onDisable() {
		storage.disconnect();
		super.onDisable();
	}

	@Override
	public DataAccess getStorage() {
		return storage;
	}

	@Override
	public Collection<SabrePlayer> getOnlinePlayers() {
		return pm.getOnlinePlayers();
	}

	@Override
	public CorePlayer getPlayer(String name) {
		return pm.getPlayerByName(name);
	}

	@Override
	public CorePlayer getPlayer(UUID uid) {
		return pm.getPlayerById(uid);
	}

	@Override
	public String formatText(String text, Object... args) {
		return textUtil.parse(text, args);
	}

	@Override
	public String formatText(String text) {
		return textUtil.parse(text);
	}

	@Override
	public TextFormatter getFormatter() {
		return textUtil;
	}

	@Override
	public SabreConfig getSabreConfig(JavaPlugin plugin) {
		return new CoreConfiguration(plugin);
	}
	
	@Override
	public SabreCommand<?> getAutoHelp() {
		return autoHelp;
	}
	
	public PlayerManager getPlayerManager() {
		return pm;
	}
	
	@Override
	public BlockPermission getBlockPermission(Block b) {
		return defaultBlockPerms;
	}

	@Override
	public BlockPermission getBlockPermission(Location l) {
		return defaultBlockPerms;
	}

	@Override
	public PlayerSpawner getSpawner() {
		return spawner;
	}

	@Override
	public PlayerVanisher getVanisher() {
		return vanisher;
	}
}
