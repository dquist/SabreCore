package com.civfactions.SabreCore.chat;

import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.civfactions.SabreApi.Lang;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.chat.ChatPlayer;
import com.civfactions.SabreApi.util.SabreUtil;

class ChatListener implements Listener {

	private final SabreApi sabreApi;

	ChatListener(SabreApi sabreApi) {

		this.sabreApi = sabreApi;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChatEvent(AsyncPlayerChatEvent e) {
		try {
			e.setCancelled(true);

			ChatPlayer player = sabreApi.getPlayer(e.getPlayer().getUniqueId());
			player.getChatChannel().chat(player, e.getMessage());

		} catch (Exception ex) {
			e.getPlayer().sendMessage(sabreApi.formatText(Lang.exceptionGeneral));
			sabreApi.log(Level.SEVERE, SabreUtil.getExceptionMessage("onPlayerChatEvent", ex));
			throw ex;
		}
	}
}
