package com.yourname.mythictotemtrigger;

import com.yourname.mythictotemtrigger.trigger.TotemPopTrigger;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.trigger.TriggerMetadata;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * Listens for EntityResurrectEvent (fires when a totem saves an entity from death)
 * and dispatches the OnTotemPop MythicMobs trigger on the affected entity.
 */
public class TotemPopListener implements Listener {

    private final JavaPlugin plugin;

    public TotemPopListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * EntityResurrectEvent fires when a totem of undying prevents death.
     * isCancelled() == true means the entity WILL die (totem was cancelled),
     * so we only act when the event is NOT cancelled (totem actually triggers).
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTotemPop(EntityResurrectEvent event) {
        Entity entity = event.getEntity();

        // Only living entities can have MythicMobs skills
        if (!(entity instanceof LivingEntity livingEntity)) return;

        // Schedule on next tick so the resurrection has fully applied
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            fireMythicTrigger(livingEntity);
        });
    }

    /**
     * Looks up whether this entity is a tracked MythicMob (or a player)
     * and fires the OnTotemPop trigger on it.
     */
    private void fireMythicTrigger(LivingEntity entity) {
        // Check if this entity is an active MythicMob
        Optional<ActiveMob> activeMobOpt = MythicBukkit.inst()
                .getMobManager()
                .getActiveMob(entity.getUniqueId());

        if (activeMobOpt.isPresent()) {
            ActiveMob activeMob = activeMobOpt.get();
            // Fire the trigger on the MythicMob — this will run any skill
            // that has ~onTotemPop defined in its mob YAML
            activeMob.getTriggerRegistry().onTrigger(
                    TotemPopTrigger.INSTANCE,
                    new TriggerMetadata(activeMob, null)
            );
            plugin.getLogger().fine("[TotemPop] Fired for MythicMob: "
                    + activeMob.getMobType().getInternalName()
                    + " (" + entity.getName() + ")");

        } else if (entity instanceof Player player) {
            // For players, check if they have a MythicMobs item with this trigger
            // (e.g. from MythicCrucible items with ~onTotemPop skills)
            // MythicMobs player skill triggers are dispatched via the API event bus
            MythicBukkit.inst().getApiHelper()
                    .getMythicMobInstance(entity.getUniqueId())
                    .ifPresent(mob -> mob.getTriggerRegistry().onTrigger(
                            TotemPopTrigger.INSTANCE,
                            new TriggerMetadata(mob, null)
                    ));

            plugin.getLogger().fine("[TotemPop] Fired for Player: " + player.getName());
        }
    }
}
