package com.yourname.mythictotemtrigger;

import com.yourname.mythictotemtrigger.trigger.TotemPopTrigger;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.trigger.TriggerRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class MythicTotemTrigger extends JavaPlugin {

    private static MythicTotemTrigger instance;

    @Override
    public void onEnable() {
        instance = this;

        // Register our custom trigger into MythicMobs
        registerTrigger();

        // Register the Bukkit listener that fires the trigger
        getServer().getPluginManager().registerEvents(new TotemPopListener(this), this);

        getLogger().info("MythicTotemTrigger enabled — OnTotemPop trigger registered!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MythicTotemTrigger disabled.");
    }

    private void registerTrigger() {
        TriggerRegistry registry = MythicBukkit.inst().getTriggerRegistry();
        registry.register(TotemPopTrigger.INSTANCE);
    }

    public static MythicTotemTrigger getInstance() {
        return instance;
    }
}
