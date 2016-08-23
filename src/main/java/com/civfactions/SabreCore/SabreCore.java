package com.civfactions.SabreCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.SabreCommand;
import com.civfactions.SabreApi.SabreModule;
import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.SabrePlugin;
import com.civfactions.SabreApi.data.DataAccess;
import com.civfactions.SabreApi.util.TextFormatter;
import com.civfactions.SabreCore.chat.ChatModule;
import com.civfactions.SabreCore.data.MongoStorage;
import com.civfactions.SabreCore.util.TextUtil;

public class SabreCore implements SabreApi {
	
	private final SabrePlugin plugin;
	
	private final HashSet<SabreModule> modules = new HashSet<SabreModule>();
	private final TextUtil textUtil = new TextUtil();
	private final DataStorage storage = new MongoStorage();
	private final PlayerManager pm = new PlayerManager(this, storage);
	private final RegisteredCommands commands = new RegisteredCommands();
	
	private final ChatModule chatModule = new ChatModule(this);
	
	public SabreCore(SabrePlugin plugin) {
		this.plugin = plugin;
		
		modules.add(chatModule);
	}

	@Override
	public void onEnable() {
		log("=== ENABLE START ===");
		long timeEnableStart = System.currentTimeMillis();
		
		// Enable the registered modules
		for(SabreModule m: modules) {
			try {
				m.onEnable();
			} catch (Throwable ex) {
				plugin.getLogger().log(Level.SEVERE, String.format("Failed to load Sabre module '%s'.",  m.getClass().getName()));
				ex.printStackTrace();
			}
		}
		
		// Startup complete!
		log("=== ENABLE DONE (Took "+(System.currentTimeMillis() - timeEnableStart)+"ms) ===");
	}
	
	@Override
	public void onDisable() {
		log("=== %s Disabled ===", plugin.getName());
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
	public void registerCommand(SabreCommand command) {
		commands.add(command);
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
	public void log(Level level, String msg, Object... args) {
		plugin.getLogger().log(level, formatText(msg, args));
	}

	@Override
	public void log(String msg, Object... args) {
		log(Level.INFO, msg, args);
	}

	@Override
	public TextFormatter getFormatter() {
		return this.textUtil;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		for (SabreCommand c : commands) {
			List<String> aliases = c.getAliases();
			if (aliases.contains(cmd.getLabel())) {

				// Set the label to the default alias
				cmd.setLabel(aliases.get(0));
				
				c.execute(sender, new ArrayList<String>(Arrays.asList(args)));
				return true;
			}
		}
		return false;
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		for (SabreCommand c : commands) {
			List<String> aliases = c.getAliases();
			if (aliases.contains(cmd.getLabel())) {

				// Set the label to the default alias
				cmd.setLabel(aliases.get(0));
				
				return c.getTabList(sender, new ArrayList<String>(Arrays.asList(args)));
			}
		}
		return null;
	}
}
