package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.event.Listener;

import com.civfactions.SabreApi.PlayerWrapper;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.SabreModule;
import com.civfactions.SabreApi.SabrePlugin;
import com.civfactions.SabreApi.data.DataAccess;
import com.civfactions.SabreCore.chat.ChatModule;
import com.civfactions.SabreCore.data.ClassStorage;
import com.civfactions.SabreCore.data.CoreStoredValue;
import com.civfactions.SabreCore.data.MongoStorage;
import com.civfactions.SabreCore.util.TextUtil;

public class SabreCore implements SabreApi {
	
	private final SabrePlugin plugin;
	
	private final HashSet<SabreModule> modules = new HashSet<SabreModule>();
	private final TextUtil textUtil = new TextUtil();
	private final DataStorage storage = new MongoStorage();
	private final ClassStorage playerStorage = new ClassStorage();
	private final PlayerManager pm = new PlayerManager(this, storage, playerStorage);
	
	private final ChatModule chatModule = new ChatModule(this);
	
	public SabreCore(SabrePlugin plugin) {
		this.plugin = plugin;
		
		modules.add(chatModule);
	}

	@Override
	public void onEnable() {
		// Enable the registered modules
		for(SabreModule m: modules) {
			try {
				m.onEnable();
			} catch (Throwable ex) {
				plugin.getLogger().log(Level.SEVERE, String.format("Failed to load Sabre module '%s'.",  m.getClass().getName()));
				ex.printStackTrace();
			}
		}
		
		playerStorage.register("lastMessaged", null);
	}

	@Override
	public DataAccess getStorage() {
		return storage;
	}

	@Override
	public Date getTimeNow() {
		return new Date();
	}
	
	
	@Override
	public void registerEvents(Listener handler) {
		plugin.registerEvents(handler);
	}

	@Override
	public <T extends PlayerWrapper> Collection<T> getOnlinePlayers() {
		//return pm.getOnlinePlayers();
		return null; // TODO
	}

	@Override
	public <T extends PlayerWrapper> T getPlayer(String name) {
		return null;  // TODO
	}

	@Override
	public <T extends PlayerWrapper> T getPlayer(UUID uid) {
		return null; // TODO
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
	public void log(Level level, String str, Object... args) {
		plugin.getLogger().log(level, formatText(str, args));
	}

	@Override
	public void log(Level level, Object msg) {
		log(level, msg);
	}

	@Override
	public void log(Object msg) {
		log(Level.INFO, msg);
	}

	@Override
	public void log(String str, Object... args) {
		log(Level.INFO, str, args);
	}
}
