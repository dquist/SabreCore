package com.civfactions.SabreCore;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.SabreCoreFactory;
import com.civfactions.SabreApi.SabrePlugin;

/**
 * The only reason this class exists is so that Spigot will load the jar
 * as a plugin and provide access to the core.
 */
public class SabreCorePlugin extends JavaPlugin implements SabreCoreFactory {

	@Override
	public SabreApi createCore(SabrePlugin plugin) {
		plugin.getLogger().log(Level.INFO, String.format("Creating new SabreCore instance for %s.", plugin.getName()));
		return new SabreCore(plugin);
	}
}
