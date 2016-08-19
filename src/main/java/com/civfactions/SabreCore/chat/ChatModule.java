package com.civfactions.SabreCore.chat;

import java.util.UUID;

import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.SabreModule;
import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.chat.ChatPlayer;
import com.civfactions.SabreApi.util.Guard;

/**
 * The Sabre Chat Module
 * @author Gordon
 *
 */
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
		this.chatListener = new ChatListener(sabreApi, this);
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
	
	
	/**
	 * Gets a player wrapped as a ChatPlayer
	 * @param name The name of the player
	 * @return The wrapper ChatPlayer if it exists, otherwise null
	 */
	public ChatPlayer getPlayer(String name) {
		return wrapPlayer(sabreApi.getPlayer(name));
	}
	
	
	/**
	 * Gets a player wrapped as a ChatPlayer
	 * @param name The name of the player
	 * @return The wrapper ChatPlayer if it exists, otherwise null
	 */
	public ChatPlayer getPlayer(UUID uid) {
		return wrapPlayer(sabreApi.getPlayer(uid));
	}
	

	
	
	/**
	 * Gets a player wrapped as a ChatPlayer
	 * @param name The name of the player
	 * @return The wrapper ChatPlayer if it exists, otherwise null
	 */
	public ChatPlayer wrapPlayer(SabrePlayer player) {
		if (player == null) {
			return null;
		}
		return new ChatPlayerWrapper(player, this);
	}
}
