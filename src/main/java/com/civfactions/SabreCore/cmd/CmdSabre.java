package com.civfactions.SabreCore.cmd;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdSabre extends CoreCommand {
	
	public CmdSabre(SabreCorePlugin plugin) {
		super(plugin);

		this.aliases.add("sabre");
		
		this.setHelpShort("The Sabre base command");
		this.optionalArgs.put("page", "1");
		
		this.visibility = CommandVisibility.SECRET;
		this.permission = Permission.ADMIN_NODE;
		
		this.addSubCommand(new CmdSabreLoadConfig(plugin));
		this.addSubCommand(new CmdSabreDeletePlayer(plugin));
		this.addSubCommand(new CmdSabreRenamePlayer(plugin));
		this.addSubCommand(new CmdSabreRespawn(plugin));
	}

	@Override
	protected void perform() {
		this.commandChain.add(this);
		plugin.getAutoHelp().execute(this.sender, this.args, this.commandChain);
	}
}
