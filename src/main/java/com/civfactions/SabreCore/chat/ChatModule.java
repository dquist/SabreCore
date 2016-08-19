package com.civfactions.SabreCore.chat;

import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.SabreModule;
import com.civfactions.SabreApi.util.Guard;

public class ChatModule extends SabreModule {

	private final SabreApi sabreApi;
	private final GlobalChat globalChat;
	private final ServerBroadcast broadcastChat;
	private final ChatListener chatListener;
	
	/**
	 * Creates a new ChatModule instance
	 * @param sabreApi The Sabre API
	 */
	public ChatModule(SabreApi sabreApi) {
		Guard.ArgumentNotNull(sabreApi, "sabreApi");
		
		this.sabreApi = sabreApi;
		this.globalChat = new GlobalChat(sabreApi);
		this.broadcastChat = new ServerBroadcast(sabreApi);
		this.chatListener = new ChatListener(sabreApi);
	}

	
	@Override
	public void onEnable() {
		
		
		
		
		sabreApi.registerEvents(chatListener);
	}
	
	
	/**
	 * Gets the global chat instance
	 * @return The global chat instance
	 */
	public GlobalChat getGlobalChat() {
		return this.globalChat;
	}
	
	
	/**
	 * Gets the server broadcast chat instance
	 * @return The server broadcast chat instance
	 */
	public ServerBroadcast getServerBroadcast() {
		return this.broadcastChat;
	}
}
