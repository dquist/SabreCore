package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;

import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.SabrePlugin;
import com.civfactions.SabreApi.data.DataAccess;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.util.TextFormatter;
import com.civfactions.SabreCore.data.MongoStorage;
import com.civfactions.SabreCore.util.TextUtil;

/**
 * This class is the provides the core Sabre classes.
 *
 */
public class SabreCorePlugin extends SabrePlugin {
	
	private final TextUtil textUtil = new TextUtil();
	private final DataStorage storage = new MongoStorage(this);
	private final PlayerManager pm = new PlayerManager(this, storage);
	
	public SabreCorePlugin() {
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		// TODO load config
		SabreDocument config = new SabreDocument();
		
		storage.fromDocument(config);
		
		//connectDatabase();
		
		super.postEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	
	private void connectDatabase() {
		try {
			storage.connect();
		} catch (Exception ex) {
			log(Level.SEVERE, "* * * Failed to connect to database! * * * ");
		}
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
}
