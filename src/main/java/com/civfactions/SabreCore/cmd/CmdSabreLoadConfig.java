package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.SabrePlugin;

public class CmdLoadConfig extends CoreCommand
{
	public CmdLoadConfig(SabrePlugin plugin) {
		super(plugin);
		
		this.senderMustBePlayer = false;
		
		this.aliases.add("load");
		this.helpShort = "Reloads the plugin configuration";
	}
	
	@Override
	public void perform() {
		long startTime = System.currentTimeMillis();
		plugin.readConfiguration();
		msg("<g>Configuration reloaded in %dms.", System.currentTimeMillis() - startTime);
	}
}
