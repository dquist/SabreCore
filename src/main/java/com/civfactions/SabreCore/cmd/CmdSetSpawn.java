package com.civfactions.SabreCore.cmd;

import org.bukkit.Location;
import org.bukkit.World;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.Lang;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdSetSpawn extends CoreCommand {

	public CmdSetSpawn(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("setspawn");

		this.setHelpShort("Sets the world spawn location");
		
		this.errorOnToManyArgs = false;
		this.senderMustBePlayer = true;
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@Override
	public void perform() {
		Location l = me().getBukkitPlayer().getLocation();
		World w = l.getWorld();
		w.setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		msg(Lang.adminSetSpawn);
	}
}
