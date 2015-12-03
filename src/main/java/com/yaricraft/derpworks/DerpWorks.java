package com.yaricraft.derpworks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Created by Yari on 12/1/2015.
 */
public class DerpWorks extends JavaPlugin {

    public static void log(String message) { log(message, Level.INFO); }
    public static void log(String message, Level level) { Bukkit.getLogger().log(level != null ? level : Level.INFO, "[DerpWorks] " + message); }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DerpWorksListener(), this);
        log("Loaded DerpWorks");
    }
}
