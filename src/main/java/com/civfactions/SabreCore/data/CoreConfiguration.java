package com.civfactions.SabreCore.data;

import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.civfactions.SabreApi.data.SabreConfig;
import com.civfactions.SabreApi.data.SabreDocument;

public class CoreConfiguration implements SabreConfig {
	
	private final JavaPlugin plugin;
	
	private SabreDocument doc;

	public CoreConfiguration(JavaPlugin plugin) {
		this.plugin = plugin;
		
		doc = configurationSectionToDocument(plugin.getConfig());
	}
	
	private SabreDocument configurationSectionToDocument(ConfigurationSection mem) {
		SabreDocument doc = new SabreDocument();
		
		for(Entry<String, Object> e : mem.getValues(false).entrySet()) {
			if (e.getValue() instanceof ConfigurationSection) {
				doc.append(e.getKey(), configurationSectionToDocument((ConfigurationSection)e.getValue()));
			}
			else {
				doc.append(e.getKey(), e.getValue());
			}
		}
		
		return doc;
	}
	
	
	private ConfigurationSection documentToConfigurationSection(ConfigurationSection mem, SabreDocument doc) {
		for(Entry<String, Object> e : doc.entrySet()) {
			if (e.getValue() instanceof SabreDocument) {
				documentToConfigurationSection(mem.createSection(e.getKey()), (SabreDocument)e.getValue());
			}
			else {
				mem.set(e.getKey(), e.getValue());
			}
		}
		
		return mem;
	}

	@Override
	public SabreDocument getDocument() {
		return this.doc;
	}

	@Override
	public SabreConfig reloadFile() {
		plugin.reloadConfig();
		doc = configurationSectionToDocument(plugin.getConfig());
		return this;
	}

	@Override
	public void saveToFile(SabreDocument doc) {
		documentToConfigurationSection(plugin.getConfig(), doc);
		
		plugin.saveConfig();
	}
	
	@Override
	public void saveToFile() {
		saveToFile(doc);
	}
}
