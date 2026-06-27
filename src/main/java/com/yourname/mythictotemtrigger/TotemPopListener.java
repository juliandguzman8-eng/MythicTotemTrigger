package com.yourname.mythictotemtrigger;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class TotemPopListener implements Listener {

    private final JavaPlugin plugin;

    public TotemPopListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTotemPop(EntityResurrectEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) return;

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            Optional<ActiveMob> activeMobOpt = MythicBukkit.inst()
                    .getMobManager()
                    .getActiveMob(livingEntity.getUniqueId());

            if (activeMobOpt.isPresent()) {
                ActiveMob mob = activeMobOpt.get();
                MythicBukkit.inst().getAPIHelper().castSkill(
                        mob.getEntity().getBukkitEntity(),
                        "onTotemPop"
                );
                plugin.getLogger().info("[TotemPop] Trigger disparado en: "
                        + mob.getMobType().getInternalName());
            }
        });
    }
}
