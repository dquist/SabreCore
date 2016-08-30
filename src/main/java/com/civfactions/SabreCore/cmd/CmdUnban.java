package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.CorePlayer;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdUnban extends CoreCommand {

	public CmdUnban(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("unban");

		this.requiredArgs.add("player");

		this.setHelpShort("Bans a player");
		
		this.errorOnToManyArgs = false;
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@Override
	public void perform() {
		String playerName = this.argAsString(0);
		
		CorePlayer p = plugin.getPlayer(playerName);
		
		if (p == null) {
			msg(Lang.unknownPlayer, playerName);
			return;
		}
		
		if (!p.getBanned()) {
			msg(Lang.adminPlayerNotBanned, p.getName());
			return;
		}
		
		p.setBanned(false, "");
		msg(Lang.adminUnbannedPlayer, p.getName());
	}
}
