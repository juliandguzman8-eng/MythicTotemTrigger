package com.yourname.mythictotemtrigger.trigger;

import io.lumine.mythic.api.skills.trigger.Trigger;
import io.lumine.mythic.api.skills.trigger.TriggerMetadata;

/**
 * Defines the OnTotemPop trigger for MythicMobs.
 *
 * Usage in MythicMobs mob/item YAML:
 *
 *   Skills:
 *   - skill{s=MySkill} @self ~onTotemPop
 *
 * This trigger fires when any player (or tracked MythicMob entity) consumes
 * a Totem of Undying to avoid death.
 */
public class TotemPopTrigger implements Trigger {

    /** Singleton instance registered into MythicMobs */
    public static final TotemPopTrigger INSTANCE = new TotemPopTrigger();

    /** The name used in YAML: ~onTotemPop */
    public static final String NAME = "ONTOTEM POP";

    private TotemPopTrigger() {}

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Returns all aliases that MythicMobs should accept for this trigger.
     * Players can use ~onTotemPop or ~onTotem in their YAML.
     */
    @Override
    public String[] getAliases() {
        return new String[]{"ONTOTEM", "TOTEM", "TOTEMPOP", "ONUNDYING"};
    }

    /**
     * We don't filter by metadata — any totem use fires this.
     */
    @Override
    public boolean isMatch(TriggerMetadata metadata) {
        return true;
    }
}
