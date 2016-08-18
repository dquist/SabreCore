package com.civfactions.SabreCore.chat;

import java.util.logging.Level;

import com.civfactions.SabreApi.IChatChannel;
import com.civfactions.SabreApi.IPlayer;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.data.ConfigurationObject;
import com.civfactions.SabreApi.data.IConfigurable;
import com.civfactions.SabreApi.data.StringConfiguration;
import com.civfactions.SabreApi.data.StringValue;
import com.civfactions.SabreApi.util.Guard;

public class GlobalChat implements IChatChannel, IConfigurable {

	private final String CONFIG_KEY = "chat";
	private final StringValue strNoOneHears = new StringValue("NO_ONE_HEARS_YOU", "<silver>No one hears you.");
	
	private final SabreApi sabreApi;
	
	private GlobalChatType chatType = GlobalChatType.RADIUS;
	private int chatRadius = 500;
	private StringConfiguration strings = new StringConfiguration();
	
	
	/**
	 * Creates a new GlobalChat instance
	 * @param sabreApi The Sabre API
	 * @param config The config object
	 */
	public GlobalChat(SabreApi sabreApi) {
		Guard.ArgumentNotNull(sabreApi, "sabreApi");
		
		this.sabreApi = sabreApi;
	}
	
	
	@Override
	public void chat(IPlayer sender, String msg) {
		String senderName = sender.getName();
		boolean found = false;
		String formatted = sabreApi.formatText("<w>%s: %s", senderName, msg);
		sabreApi.log(Level.INFO, formatted);
		
		for (IPlayer p : sabreApi.getOnlinePlayers()) {
			int distance = p.getDistanceFrom(sender);
			if (distance >=0 && distance <= chatRadius && p.getBukkitPlayer().getWorld().equals(sender.getBukkitPlayer().getWorld())) {
				p.msg(formatted);
				
				if (!p.equals(sender)) {
					found = true;
				}
			}
		}
		
		// So sad :(
		if (!found) {
			sender.msg(strings.get(strNoOneHears));
		}
	}
	
	
	@Override
	public void chatMe(IPlayer sender, String msg) {
		String senderName = sender.getName();
		boolean found = false;
		String formatted = sabreApi.formatText("<silver><it>%s %s", senderName, msg);
		
		for (IPlayer p : sabreApi.getOnlinePlayers()) {
			int distance = p.getDistanceFrom(sender);
			if (distance >=0 && distance <= chatRadius && p.getBukkitPlayer().getWorld().equals(sender.getBukkitPlayer().getWorld())) {
				p.msg(formatted);
				
				if (!p.equals(sender)) {
					found = true;
				}
			}
		}
		
		// So sad :(
		if (!found) {
			sender.msg(strings.get(strNoOneHears));
		}
	}

	@Override
	public String getConfigurationKey() {
		return CONFIG_KEY;
	}


	@Override
	public void loadConfiguration(ConfigurationObject config) {
		chatType = GlobalChatType.fromString(config.getString("chat_type", chatType.toString()));
		chatRadius = config.getInt("radius", chatRadius);
		strings = config.getStrings();
	}


	@Override
	public void saveConfiguration(ConfigurationObject config) {
		config.set("radius", chatRadius);
	}
}
