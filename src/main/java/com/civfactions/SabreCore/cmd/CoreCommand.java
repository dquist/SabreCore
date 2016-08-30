package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.SabreCommand;
import com.civfactions.SabreApi.SabrePlugin;

public abstract class CoreCommand extends SabreCommand<SabrePlugin> {
	
	public CoreCommand(SabrePlugin plugin) {
		super(plugin);
	}
}
