package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.SabrePlugin;
import com.civfactions.SabreApi.util.Permission;

public class CmdSabre extends CoreCommand {
	
	public CmdSabre(SabrePlugin plugin) {
		super(plugin);

		this.aliases.add("sabre");
		
		this.setHelpShort("The Sabre base command");
		this.optionalArgs.put("page", "1");
		
		this.visibility = CommandVisibility.SECRET;
		this.permission = Permission.ADMIN_NODE;
		
		this.addSubCommand(new CmdLoadConfig(plugin));
	}

	@Override
	protected void perform() {
		this.commandChain.add(this);
		plugin.getAutoHelp().execute(this.sender, this.args, this.commandChain);
	}
}
