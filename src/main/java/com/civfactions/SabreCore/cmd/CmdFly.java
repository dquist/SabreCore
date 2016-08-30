package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdFly extends CoreCommand {

	public CmdFly(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("fly");

		this.setHelpShort("Toggles fly status");
		
		this.errorOnToManyArgs = false;
		this.senderMustBePlayer = true;
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@Override
	public void perform() 
	{
		if (me().getBukkitPlayer().isFlying()) {
			me().getBukkitPlayer().setFlying(false);
			me().getBukkitPlayer().setAllowFlight(false);
			me().msg(Lang.adminFlyOff);
		} else {
			me().getBukkitPlayer().setAllowFlight(true);
			me().getBukkitPlayer().setFlying(true);
			me().msg(Lang.adminFlyOn);
		}
	}
}
