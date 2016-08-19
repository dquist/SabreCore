package com.civfactions.SabreCore.chat;

import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.chat.ChatChannel;
import com.civfactions.SabreApi.chat.ChatPlayer;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.data.ConfigurationObject;
import com.civfactions.SabreApi.data.Configurable;
import com.civfactions.SabreApi.data.StringConfiguration;
import com.civfactions.SabreApi.data.StringValue;
import com.civfactions.SabreApi.util.Guard;

class ServerBroadcast implements ChatChannel, Configurable {
	
	private final String CONFIG_KEY = "chat";
	private final StringValue strBcastChat = new StringValue("SERVER_BROADCAST_CHAT", "<gold>## %s: <w>%s");
	private final StringValue strBcastChatMe = new StringValue("SERVER_BROADCAST_CHAT_ME", "<h><it>%s %s");

	private final SabreApi sabreApi;
	
	private StringConfiguration strings = new StringConfiguration();
	
	ServerBroadcast(SabreApi sabreApi) {
		Guard.ArgumentNotNull(sabreApi, "sabreApi");
		
		this.sabreApi = sabreApi;
	}
	
	
	@Override
	public void chat(ChatPlayer sender, String msg) {
		String senderName = sender.getName();
		
		String formatted = sabreApi.formatText(strings.get(strBcastChat), senderName, msg);
		
		for (SabrePlayer p : sabreApi.getOnlinePlayers()) {
			p.msg(formatted);
		}
	}
	
	
	@Override
	public void chatMe(ChatPlayer sender, String msg) {
		String senderName = sender.getName();
		String formatted = sabreApi.formatText(strings.get(strBcastChatMe), senderName, msg);
		
		for (SabrePlayer p : sabreApi.getOnlinePlayers()) {
			p.msg(formatted);
		}
	}

	@Override
	public String getConfigurationKey() {
		return CONFIG_KEY;
	}


	@Override
	public void loadConfiguration(ConfigurationObject config) {
		strings = config.getStrings();
	}


	@Override
	public void saveConfiguration(ConfigurationObject config) {	}
}
