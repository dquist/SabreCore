package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

import com.civfactions.SabreApi.IChatChannel;
import com.civfactions.SabreApi.PlayerWrapper;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.SabrePlugin;
import com.civfactions.SabreApi.chat.ChatModule;
import com.civfactions.SabreApi.data.DataAccess;
import com.civfactions.SabreCore.chat.GlobalChat;
import com.civfactions.SabreCore.data.ClassStorage;
import com.civfactions.SabreCore.data.MongoStorage;
import com.civfactions.SabreCore.util.TextUtil;

public class SabreCore implements SabreApi {
	
	private final SabrePlugin plugin;
	
	private final TextUtil textUtil = new TextUtil();
	private final DataStorage storage = new MongoStorage();
	private final ClassStorage playerStorage = new ClassStorage();
	private final PlayerManager pm = new PlayerManager(this, storage, playerStorage);
	private final GlobalChat globalChat = new GlobalChat(this);
	private final ChatModule chatModule = new ChatModule();
	
	public SabreCore(SabrePlugin plugin) {
		this.plugin = plugin;
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
	public IChatChannel getGlobalChat() {
		return globalChat;
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
