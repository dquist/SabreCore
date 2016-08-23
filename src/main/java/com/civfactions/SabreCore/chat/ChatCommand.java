package com.civfactions.SabreCore.chat;

import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.SabreCommand;
import com.civfactions.SabreApi.chat.ChatPlayer;

abstract class ChatCommand extends SabreCommand {

	protected final ChatModule chat;
	
	public ChatCommand(SabreApi sabreApi, ChatModule chat) {
		super(sabreApi);
		this.chat = chat;
	}
	
	
	/**
	 * Gets a player instance from a string
	 * @param name The name to find
	 * @return The best match player instance
	 */
	@Override
	protected ChatPlayer strAsPlayer(String name) {
		return chat.wrapPlayer(super.strAsPlayer(name));
	}
	
	/**
	 * Gets the sender instance
	 * @return The sender instance
	 */
	@Override
	protected ChatPlayer me() {
		return chat.wrapPlayer(super.me());
	}	
}
