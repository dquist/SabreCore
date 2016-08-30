package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.CorePlayer;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdSabreDeletePlayer extends CoreCommand {

	public CmdSabreDeletePlayer(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("delete");

		this.requiredArgs.add("player");

		this.setHelpShort("Deletes the player from the sever");

		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@Override
	public void perform() 
	{
		String playerName = this.argAsString(0);
		
		CorePlayer p = plugin.getPlayer(playerName);
		
		if (p == null) {
			msg(Lang.unknownPlayer, playerName);
			return;
		}
		
		// This will kick the player and prevent a re-login
		p.setBanned(true, Lang.adminPlayerModifyBan);

		p.setBanned(false, "");
		plugin.getPlayerManager().deletePlayer(p);
		msg(Lang.adminRemovedPlayer, p.getName());
	}
}
