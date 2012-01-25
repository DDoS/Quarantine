package me.DDoS.Quarantine.listener;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 *
 * @author DDoS
 */
public class QEntityListener implements Listener {

    private final Quarantine plugin;

    public QEntityListener(Quarantine plugin) {

        this.plugin = plugin;


    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        for (QZone zone : plugin.getZones()) {

            if (zone.passCreatureSpawnEvent(event)) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof LivingEntity) {

            LivingEntity ent = (LivingEntity) event.getEntity();

            if (ent instanceof Player) {

                Player player = (Player) ent;

                for (QZone zone : plugin.getZones()) {

                    if (zone.passPlayerDeathEvent(player, event)) {

                        return;

                    }
                }

            } else {

                for (QZone zone : plugin.getZones()) {

                    if (zone.passEntityDeathEvent(ent, event)) {

                        return;

                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityCombust(EntityCombustEvent event) {

        for (QZone zone : plugin.getZones()) {

            if (zone.passEntityCombustEvent(event)) {

                return;

            }
        }
    }
}
