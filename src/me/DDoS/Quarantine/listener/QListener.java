package me.DDoS.Quarantine.listener;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 *
 * @author DDoS
 */
public class QListener implements Listener {

    private final Quarantine plugin;

    public QListener(Quarantine plugin) {

        this.plugin = plugin;


    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {

        for (Zone zone : plugin.getZones()) {

            if (zone.passPlayerTeleportEvent(event)) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        for (Zone zone : plugin.getZones()) {

            if (zone.passPlayerRespawnEvent(event, plugin)) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {

        for (Zone zone : plugin.getZones()) {

            if (zone.passPlayerQuitEvent(event.getPlayer())) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {

        for (Zone zone : plugin.getZones()) {

            if (zone.passPlayerQuitEvent(event.getPlayer())) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {

        for (Zone zone : plugin.getZones()) {

            if (zone.passPlayerInteractEvent(event)) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        for (Zone zone : plugin.getZones()) {

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

                for (Zone zone : plugin.getZones()) {

                    if (zone.passPlayerDeathEvent(player, event)) {

                        return;

                    }
                }

            } else {

                for (Zone zone : plugin.getZones()) {

                    if (zone.passEntityDeathEvent(ent, event)) {

                        return;

                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityCombust(EntityCombustEvent event) {

        for (Zone zone : plugin.getZones()) {

            if (zone.passEntityCombustEvent(event)) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkUnload(ChunkUnloadEvent event) {

        for (Zone zone : plugin.getZones()) {

            if (zone.passChunkUnloadEvent(event)) {

                return;

            }
        }
    }
}
