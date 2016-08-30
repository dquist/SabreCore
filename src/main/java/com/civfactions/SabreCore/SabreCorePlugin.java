package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.civfactions.SabreApi.SabreCommand;
import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.SabrePlugin;
import com.civfactions.SabreApi.data.DataAccess;
import com.civfactions.SabreApi.data.SabreConfig;
import com.civfactions.SabreApi.util.TextFormatter;
import com.civfactions.SabreCore.cmd.CmdAutoHelp;
import com.civfactions.SabreCore.cmd.CmdSabre;
import com.civfactions.SabreCore.data.CoreConfiguration;
import com.civfactions.SabreCore.data.MongoStorage;
import com.civfactions.SabreCore.util.TextUtil;

/**
 * This class is the provides the core Sabre classes.
 *
 */
public class SabreCorePlugin extends SabrePlugin {
	
	private final TextUtil textUtil = new TextUtil();
	private final CmdAutoHelp autoHelp = new CmdAutoHelp(this);
	private DataStorage storage;
	private PlayerManager pm;
	
	public SabreCorePlugin() {
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		storage = new MongoStorage(this);
		pm = new PlayerManager(this, storage);
		
		// Register core command
		registerCommand(new CmdSabre(this));
		
		// Register configuration objects
		registerConfigObject(storage);
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
	public SabrePlayer getPlayer(String name) {
		return pm.getPlayerByName(name);
	}

	@Override
	public SabrePlayer getPlayer(UUID uid) {
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
		return this.textUtil;
	}

	@Override
	public SabreConfig getSabreConfig(JavaPlugin plugin) {
		return new CoreConfiguration(plugin);
	}
	
	@Override
	public SabreCommand<?> getAutoHelp() {
		return autoHelp;
	}
}
