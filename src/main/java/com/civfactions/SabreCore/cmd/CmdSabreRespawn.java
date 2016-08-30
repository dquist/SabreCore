package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdSabreRespawn extends CoreCommand {

	public CmdSabreRespawn(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("respawn");
		this.optionalArgs.put("player", "you");

		this.setHelpShort("Random spawns you");
		
		this.errorOnToManyArgs = false;
		
		
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@Override
	public void perform() {
		if (args.size() == 0 && senderIsConsole) {
			msg(Lang.adminConsoleNotAllowed);
			return;
		}
		
		SabrePlayer p = me();
		
		if (args.size() > 0) {
			p = plugin.getPlayer(args.get(0));
		}
		
		if (p == null || !p.isOnline()) {
			msg(Lang.unknownPlayer, args.get(0));
			return;
		}
		
		plugin.getSpawner().spawnPlayerRandom(me());
		msg(Lang.adminRespawnedPlayer, p);
	}
}
