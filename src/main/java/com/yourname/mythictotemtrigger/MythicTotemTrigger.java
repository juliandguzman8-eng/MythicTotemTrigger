package com.yourname.mythictotemtrigger;

import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MythicTotemTrigger extends JavaPlugin {

    private static MythicTotemTrigger instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new TotemPopListener(this), this);
        getLogger().info("MythicTotemTrigger habilitado — trigger ~onTotemPop listo!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MythicTotemTrigger deshabilitado.");
    }

    public static MythicTotemTrigger getInstance() {
        return instance;
    }
}
