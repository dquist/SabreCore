package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.CorePlayer;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdBan extends CoreCommand {

	public CmdBan(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("ban");

		this.requiredArgs.add("player");
		this.requiredArgs.add("reason");

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
		
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < args.size(); i++) {
			sb.append(args.get(i));
			sb.append(" ");
		}
		
		String reason = sb.toString();
		p.setBanned(true, reason);
		msg(Lang.adminBannedPlayer, p.getName(), reason);
	}
}
