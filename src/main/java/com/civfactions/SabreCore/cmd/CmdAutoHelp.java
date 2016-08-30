package com.civfactions.SabreCore.cmd;

import java.util.ArrayList;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.SabreCommand;
import com.civfactions.SabreApi.SabrePlugin;

public class CmdAutoHelp extends CoreCommand
{
	public CmdAutoHelp(SabrePlugin plugin) {
		super(plugin);
		
		this.aliases.add("?");
		this.aliases.add("h");
		this.aliases.add("help");
		
		this.setHelpShort("");
		
		this.optionalArgs.put("page","1");
	}
	
	@Override
	public void perform() {
		if (this.commandChain.size() == 0) return;
		SabreCommand<?> cmd = this.commandChain.get(this.commandChain.size()-1);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		lines.addAll(cmd.getLongHelp());
		
		for(SabreCommand<?> c : cmd.getSubCommands()) {
			// Only list help for commands that are visible or the sender has permission for
			if (c.getVisibility() == CommandVisibility.VISIBLE || (c.getVisibility() == CommandVisibility.SECRET && c.validSenderPermissions(sender, false))) {
				lines.add(c.getUseageTemplate(this.commandChain, true));
			}
		}
		
		int page = this.argAsInt(0, 1);
		
		msg(plugin.getFormatter().getPage(lines, page, "Help for command \""+cmd.getAliases().get(0)+"\""));
	}
}
