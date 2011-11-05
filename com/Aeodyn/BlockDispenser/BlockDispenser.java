package com.Aeodyn.BlockDispenser;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.event.*;

@SuppressWarnings("deprecation")
public class BlockDispenser extends JavaPlugin {
	public static PluginManager pm = null;
	private final BlockDispenserBlockListener blockListener = new BlockDispenserBlockListener(this);

	public void onEnable(){ 
		log.info("BlockDispenser has been enabled.");
		pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_DISPENSE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Event.Priority.Normal, this);
		setup();
		if (debug) {
			log.info("BlockDispenser is debugging");
		}
	} 

	public void onDisable(){ 
		log.info("BlockDispenser has been disabled.");
	}

	static Logger log = Logger.getLogger("Minecraft");

	static boolean push = false;
	static int pushDistance = 5;
	static boolean useItem = false;
	static int throwDistance = 5;
	static boolean debug = false;
	static boolean removes = true;

	public void setup() {
		if (!new File("plugins/BlockDispenser").exists())
		{
			new File("plugins/BlockDispenser").mkdir();
			Configuration config = new Configuration(new File("plugins/BlockDispenser", "config.yml"));
			config.load();
			config.setProperty("options.Removes", true);
			config.setProperty("options.Debug", false);
			config.save();
			//        	useItem = config.getBoolean("options.Uses Items", false);
			//        	throwDistance = config.getInt("options.PushDistance", 5);
			debug = config.getBoolean("options.Debug", false);
			removes = config.getBoolean("options.Removes", true);
		} 
		else
		{
			Configuration config = new Configuration(new File("plugins/BlockDispenser", "config.yml"));
			config.load();
			//        	useItem = config.getBoolean("options.Throws", false);
			//        	throwDistance = config.getInt("options.PushDistance", 5);
			debug = config.getBoolean("options.Debug", false);
			removes = config.getBoolean("options.Removes", true);
		}
	}
}