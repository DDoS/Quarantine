package me.DDoS.Quarantine.listener;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

        if (!event.hasBlock()) {

            return;

        }

        Player player = event.getPlayer();

        if (!QUtil.checkForSign(event.getClickedBlock())) {

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                    || event.getAction() == Action.LEFT_CLICK_BLOCK
                    && event.getClickedBlock().getType() == Material.STONE_BUTTON) {

                for (Zone zone : plugin.getZones()) {

                    if (zone.passPlayerInteractButtonEvent(event, player)) {

                        return;

                    }
                }
            }

            return;

        }
        
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            
            return;
            
        }

        Sign sign = (Sign) event.getClickedBlock().getState();
        
        for (Zone zone : plugin.getZones()) {

            if (zone.passPlayerInteractSignEvent(event, player, sign)) {

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

        if (event.getEntity() instanceof Monster) {

            Monster ent = (Monster) event.getEntity();

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void EntityCombustByBlockEvent(EntityCombustByBlockEvent event) {

        for (Zone zone : plugin.getZones()) {

            if (zone.passEntityCombustByBlockEvent(event.getEntity())) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void EntityCombustByEntityEvent(EntityCombustByEntityEvent event) {

        Entity combuster = event.getCombuster();
        Player player = null;

        if (combuster instanceof Projectile) {

            LivingEntity shooter = ((Projectile) combuster).getShooter();

            if (shooter instanceof Player) {

                player = (Player) shooter;

            }

        } else if (combuster instanceof Player) {

            player = (Player) combuster;

        }

        if (player == null) {

            return;

        }

        Entity victim = event.getEntity();

        for (Zone zone : plugin.getZones()) {

            if (zone.passEntityCombustByPlayerEvent(player, victim)) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityCombust(EntityCombustEvent event) {

        if (event instanceof EntityCombustByBlockEvent
                || event instanceof EntityCombustByEntityEvent) {
            
            return;
            
        }
        
        Entity entity = event.getEntity();

        if (entity instanceof Monster) {

            Monster mob = (Monster) entity;

            for (Zone zone : plugin.getZones()) {

                if (zone.passEntityCombustEvent(event, mob)) {

                    return;

                }
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
