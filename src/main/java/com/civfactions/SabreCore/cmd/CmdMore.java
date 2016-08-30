package com.civfactions.SabreCore.cmd;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.civfactions.SabreApi.CommandVisibility;
import com.civfactions.SabreApi.util.Permission;
import com.civfactions.SabreCore.SabreCorePlugin;

public class CmdMore extends CoreCommand {

	public CmdMore(SabreCorePlugin plugin) {
		super(plugin);
		
		this.aliases.add("more");

		this.setHelpShort("Gives more items");
		
		this.errorOnToManyArgs = false;
		this.senderMustBePlayer = true;
		this.permission = Permission.ADMIN.node;
		this.visibility = CommandVisibility.SECRET;
	}

	@Override
	public void perform() 
	{
		PlayerInventory inv = me().getBukkitPlayer().getInventory();
		ItemStack item = inv.getItemInHand();
		item.setAmount(64);
		inv.setItemInHand(item);
	}
}
