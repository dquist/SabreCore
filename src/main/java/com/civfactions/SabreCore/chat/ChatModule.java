package com.civfactions.SabreCore.chat;

import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.util.Guard;

public class ChatModule {

	
	private final SabreApi sabreApi;
	private final GlobalChat globalChat;
	
	/**
	 * Creates a new ChatModule instance
	 * @param sabreApi The Sabre API
	 */
	public ChatModule(SabreApi sabreApi) {
		Guard.ArgumentNotNull(sabreApi, "sabreApi");
		
		this.sabreApi = sabreApi;
		globalChat = new GlobalChat(this.sabreApi);
	}
	
	public void register() {
		
	}
	
	/**
	 * Gets the global chat instance
	 * @return The global chat instance
	 */
	public GlobalChat getGlobalChat() {
		return this.globalChat;
	}

}
