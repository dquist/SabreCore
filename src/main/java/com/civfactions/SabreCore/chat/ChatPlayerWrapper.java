package com.civfactions.SabreCore.chat;

import java.util.Collection;
import java.util.logging.Level;

import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.chat.ChatChannel;
import com.civfactions.SabreApi.chat.ChatPlayer;
import com.civfactions.SabreApi.util.Guard;
import com.civfactions.SabreApi.Lang;
import com.civfactions.SabreApi.PlayerWrapper;

class ChatPlayerWrapper extends PlayerWrapper implements ChatPlayer {

	private final static String lastMessagedParamName = "lastMessaged";
	private final static String chatChannelParamName = "chatChannel";
	private final static String ignoredPlayersParamName = "ignoredPlayers";
	
	private final ChatModule chatModule;
	private final SabrePlayer player;
	
	ChatPlayerWrapper(SabrePlayer player, ChatModule chatModule) {
		super(player);

		Guard.ArgumentNotNull(chatModule, "chatModule");
		Guard.ArgumentNotNull(player, "player");

		this.chatModule = chatModule;
		this.player = player;
	}
	
	/**
	 * Gets the last messaged player
	 * @return The last messaged player
	 */
	public ChatPlayer getLastMessaged() {
		return player.getDataValue(lastMessagedParamName);
	}
	
	
	/**
	 * Sets the last messaged player
	 * @param lastMessaged The last messaged player
	 */
	@Override
	public void setLastMessaged(ChatPlayer lastMessaged) {
		player.setDataValue(lastMessagedParamName, lastMessaged);
	}
	
	/**
	 * Gets the current chat channel
	 * This could be global chat, a player, a group, etc
	 * @return The current chat channel
	 */
	public ChatChannel getChatChannel() {
		return player.getDataValue(chatChannelParamName);
	}
	
	
	/**
	 * Gets the current chat channel
	 * This could be global chat, a player, a group, etc
	 * @param chatChannel The new chat channel
	 */
	public void setChatChannel(ChatChannel chatChannel) {
		Guard.ArgumentNotNull(chatChannel, "chatChannel");
		Guard.ArgumentNotEquals(chatChannel, "chatChannel", this, "self");
		
		player.setDataValue(chatChannelParamName, chatChannel);
	}


	/**
	 * Gets the offline messages for the player
	 * @return The offline messages
	 */
	@Override
	public Collection<String> getOfflineMessages() {
		return null; // TODO
	}
	
	
	/**
	 * Sets the chat channel to global chat
	 */
	@Override
	public void moveToGlobalChat() {
		setChatChannel(chatModule.getGlobalChat());
	}
	
	
	@Override
	public void setIgnored(ChatPlayer player, boolean ignored) {
		Guard.ArgumentNotNull(player, "player");
		
		/*
		if (ignored && !ignoredPlayers.contains(sp)) {
			ignoredPlayers.add(sp);
		} else {
			ignoredPlayers.remove(sp);
		} */
		// TODO
	}
	
	
	/**
	 * Gets whether a player is ignored
	 * @return Whether the player is ignored
	 */
	@Override
	public boolean isIgnoring(ChatPlayer player) {
		Guard.ArgumentNotNull(player, "player");
		return false; // TODO
	}


	@Override
	public void chat(ChatPlayer sender, String msg) {
		Guard.ArgumentNotNull(sender, "sender");
		Guard.ArgumentNotNull(msg, "msg");
		
		if (this.isOnline()) {
			if (isIgnoring(sender)) {
				sender.msg(Lang.chatYouAreIgnored, this.getName());
				
				// Move to global chat
				if (sender.getChatChannel().equals(this)) {
					sender.moveToGlobalChat();
					sender.msg(Lang.chatMovedGlobal);
				}
				return;
			}
			
			sender.msg("<lp>To %s: %s", this.getName(), msg);
			this.msg("<lp>From %s: %s", sender.getName(), msg);
			this.setLastMessaged(sender);
			sender.setLastMessaged(this);
			player.getSabreApi().log(Level.INFO, "%s -> %s: %s", sender.getName(), this.getName(), msg);
		} else {
			sender.msg(Lang.chatPlayerNowOffline, this.getName());
			
			// Move to global chat
			if (sender.getChatChannel().equals(this)) {
				sender.moveToGlobalChat();
				sender.msg(Lang.chatMovedGlobal);
			}
		}
	}
	
	
	@Override
	public void chatMe(ChatPlayer sender, String msg) {		
		Guard.ArgumentNotNull(sender, "sender");
		Guard.ArgumentNotNull(msg, "msg");
		
		if (this.isOnline()) {
			sender.msg("<lp><it>%s %s", this.getName(), msg);
			this.msg("<lp><it>%s %s", sender.getName(), msg);
			this.setLastMessaged(sender);
			sender.setLastMessaged(this);
			player.getSabreApi().log(Level.INFO, "%s -> %s: %s", sender.getName(), this.getName(), msg);
		} else {
			sender.msg(Lang.chatPlayerNowOffline, this.getName());
			sender.msg(Lang.chatMovedGlobal, this.getName());
		}
	}
}
