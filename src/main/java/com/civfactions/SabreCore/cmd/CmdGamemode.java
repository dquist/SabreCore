package com.civfactions.SabreCore.cmd;

import org.bukkit.GameMode;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdGamemode extends CoreCommand {

	public CmdGamemode(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("gamemode");
		this.aliases.add("gm");

		this.setHelpShort("Set game mode");
		
		this.requiredArgs.add("mode");
		
		this.errorOnToManyArgs = false;
		this.senderMustBePlayer = true;
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void perform() 
	{
		Integer modeNum = this.argAsInt(0);
		GameMode mode = null;
		
		try {
			if (modeNum != null) {
				if (modeNum < 0 || modeNum > 4) {
					msg(Lang.adminInvalidMode);
					return;
				}
				mode = GameMode.getByValue(modeNum);
			}
			
			if (mode == null) {
				mode = GameMode.valueOf(this.args.get(0).toUpperCase());
			}
		}
		finally {
			if (mode == null) {
				msg(Lang.adminInvalidMode);
				return;
			}
		}
		
		
		me().getBukkitPlayer().setGameMode(mode);
		msg(Lang.adminUpdatedMode, mode.toString());
	}
}
