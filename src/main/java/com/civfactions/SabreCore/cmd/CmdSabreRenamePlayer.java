package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.CorePlayer;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdSabreRenamePlayer extends CoreCommand {

	public CmdSabreRenamePlayer(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("rename");

		this.requiredArgs.add("player");
		this.requiredArgs.add("new name");

		this.setHelpShort("Changes the name for a player on this server");
		
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@Override
	public void perform() 
	{
		String playerName = this.argAsString(0);
		String newName = this.argAsString(1);
		
		CorePlayer p = plugin.getPlayer(playerName);
		
		if (p == null) {
			msg(Lang.unknownPlayer, playerName);
			return;
		}
		
		// Try to get an exact match for the new name
		CorePlayer existing = plugin.getPlayer(newName);
		
		if (existing != null && !p.equals(existing)) {
			msg(Lang.adminNameExists, newName);
			return;
		}
		
		p.setName(newName);
		p.msg(Lang.adminYourNameIsNow, newName);
		msg(Lang.adminChangedPlayerName, playerName, newName);
	}
}
