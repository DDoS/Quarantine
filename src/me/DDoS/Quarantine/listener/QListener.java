package me.DDoS.Quarantine.listener;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.LazyMetadataValue;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.gui.SpoutEnabledGUIHandler;
import me.DDoS.Quarantine.player.CallablePlayer;
import me.DDoS.Quarantine.player.PlayerType;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 *
 * @author DDoS
 */
public class QListener implements Listener {

    private final Quarantine plugin;

    public QListener(Quarantine plugin) {

        this.plugin = plugin;


    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {

        Zone zone = plugin.getZoneByPlayer(event.getPlayer().getName());

        if (zone != null) {

            zone.handlePlayerTeleport(event);

        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Zone zone = plugin.getZoneByPlayer(event.getPlayer().getName());

        if (zone != null) {

            zone.handlePlayerQuit(event.getPlayer());

        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {

        Zone zone = plugin.getZoneByPlayer(event.getPlayer().getName());

        if (zone != null) {

            zone.handlePlayerQuit(event.getPlayer());

        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.hasBlock()) {

            return;

        }

        Zone zone = plugin.getZoneByPlayer(event.getPlayer().getName());

        if (zone == null) {

            return;

        }

        if (!QUtil.checkForSign(event.getClickedBlock())) {

            if ((event.getAction() == Action.RIGHT_CLICK_BLOCK
                    || event.getAction() == Action.LEFT_CLICK_BLOCK)
                    && event.getClickedBlock().getType() == Material.STONE_BUTTON) {

                zone.handlePlayerInteractButton(event, event.getPlayer());

            }

            return;

        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {

            return;

        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (!sign.getLine(0).equalsIgnoreCase("[Quarantine]")) {

            return;

        }

        zone.handlePlayerInteractSign(event, sign);

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        if (!event.getSpawnReason().equals(SpawnReason.CUSTOM)) {

            event.setCancelled(true);

        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {

        Zone zone = plugin.getZoneByPlayer(event.getEntity().getName());

        if (zone != null) {

            zone.handlePlayerDeath(event);

        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {

        Zone zone = plugin.getZoneByMob(event.getEntity());

        if (zone != null) {

            zone.handleEntityDeath(event);

        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityCombustByBlock(EntityCombustByBlockEvent event) {

        if (event.getEntity().hasMetadata("quarantine_fire_damager")) {

            event.getEntity().removeMetadata("quarantine_fire_damager", plugin);

        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {

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

        if (plugin.isQuarantinePlayer(player.getName())) {

            event.getEntity().setMetadata("quarantine_fire_damager",
                    new LazyMetadataValue(plugin, new CallablePlayer(player)));

        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityCombust(EntityCombustEvent event) {

        if (event instanceof EntityCombustByBlockEvent
                || event instanceof EntityCombustByEntityEvent) {

            return;

        }

        Entity entity = event.getEntity();

        if (entity instanceof LivingEntity) {

            if (plugin.getZoneByMob((LivingEntity) entity) != null) {

                event.setCancelled(true);

            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {

        Zone zone = plugin.getZoneByChunk(event.getChunk());

        if (zone != null) {

            zone.handleChunkUnload(event);

        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        PlayerType type;

        if (plugin.isQuarantinePlayer(event.getPlayer().getName())) {

            type = plugin.getQuarantinePlayer(event.getPlayer().getName()).getType();

        } else {

            type = PlayerType.DEFAULT_PLAYER;

        }

        event.getItemDrop().setMetadata("quarantine_item_owner_player_type",
                new LazyMetadataValue(plugin, type));

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {

        if (!event.getItem().hasMetadata("quarantine_item_owner_player_type")) {

            return;

        }

        PlayerType pickerType = plugin.isQuarantinePlayer(event.getPlayer().getName())
                ? plugin.getQuarantinePlayer(event.getPlayer().getName()).getType()
                : PlayerType.DEFAULT_PLAYER;

        PlayerType ownerType = (PlayerType) event.getItem().getMetadata(
                "quarantine_item_owner_player_type").get(0).value();

        if (pickerType == PlayerType.DEFAULT_PLAYER
                && (ownerType == PlayerType.LOBBY_PLAYER
                || ownerType == PlayerType.ZONE_PLAYER)) {

            event.setCancelled(true);

        } else if ((pickerType == PlayerType.LOBBY_PLAYER
                || pickerType == PlayerType.ZONE_PLAYER)
                && ownerType == PlayerType.DEFAULT_PLAYER) {

            event.setCancelled(true);

        }
    }
}
