package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdSabreLoadConfig extends CoreCommand
{
	public CmdSabreLoadConfig(SabreCorePlugin plugin) {
		super(plugin);
		
		this.senderMustBePlayer = false;
		
		this.aliases.add("load");
		this.helpShort = "Reloads the plugin configuration";
		
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}
	
	@Override
	public void perform() {
		long startTime = System.currentTimeMillis();
		plugin.readConfiguration();
		msg("<g>Configuration reloaded in %dms.", System.currentTimeMillis() - startTime);
	}
}
