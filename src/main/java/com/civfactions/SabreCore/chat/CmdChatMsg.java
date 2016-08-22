package com.civfactions.SabreCore.chat;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.Lang;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.chat.ChatPlayer;

public class CmdChatMsg extends ChatCommand {
	
	
	public CmdChatMsg(SabreApi sabreApi, ChatModule chat) {
		super(sabreApi, chat);
		this.aliases.add("msg");

		this.optionalArgs.put("player", "");
		this.optionalArgs.put("what", "");
		
		this.helpShort = "messages a player";

		senderMustBePlayer = true;
		errorOnToManyArgs = false;
		this.visibility = CommandVisibility.INVISIBLE;
	}

	
	@Override
	public void perform() {
		if (args.size() == 0) {
			msg(Lang.chatMovedGlobal);
			me().moveToGlobalChat();
			return;
		}
		
		String playerName = this.argAsString(0);
		ChatPlayer p = this.strAsPlayer(playerName);
		
		if (p == null) {
			msg(Lang.unknownPlayer, playerName);
			return;
		} 
		
		if (!p.isOnline()) {
			msg(Lang.chatPlayerOffline, p.getName());
			return;
		}
		
		if(me().isIgnoring(p)) {
			me().setIgnored(p, false);
			me().msg(Lang.chatStoppedIgnoring, p.getName());
		}
		
		if (args.size() == 1) {
			me().setChatChannel(p);
			msg(Lang.chatChattingWith, p.getName());
		} else {
			
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.size(); i++) {
				sb.append(args.get(i));
				sb.append(" ");
			}
			
			p.chat(me(), sb.toString().trim());
		}
	}
}
