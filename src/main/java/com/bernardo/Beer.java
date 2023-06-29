package com.bernardo;

import org.bukkit.plugin.java.JavaPlugin;

public final class Beer extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Starting Beer Plugin");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}