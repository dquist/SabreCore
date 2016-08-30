package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.PlayerVanisher;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdVanish extends CoreCommand {

	public CmdVanish(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("vanish");

		this.setHelpShort("Toggles vanish status");
		
		this.errorOnToManyArgs = false;
		this.senderMustBePlayer = true;
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@Override
	public void perform() {
		PlayerVanisher pv = plugin.getVanisher();
		
		if (pv.isVanished(me())) {
			pv.unVanish(me());
			msg(Lang.adminUnvanished);
		} else {
			pv.vanish(me());
			msg(Lang.adminVanished);
		}
	}
}
