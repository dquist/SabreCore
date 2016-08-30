package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.SabreCommand;
import com.civfactions.SabreCore.SabreCorePlugin;

public abstract class CoreCommand extends SabreCommand<SabreCorePlugin> {
	
	public CoreCommand(SabreCorePlugin plugin) {
		super(plugin);
	}
}
