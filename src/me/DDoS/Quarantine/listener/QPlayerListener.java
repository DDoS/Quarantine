package me.DDoS.Quarantine.listener;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author DDoS
 */
public class QPlayerListener implements Listener {

    private final Quarantine plugin;

    public QPlayerListener(Quarantine plugin) {

        this.plugin = plugin;

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        
        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerTeleportEvent(event)) {
                
                return;
                
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        
        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerRespawnEvent(event, plugin)) {
                
                return;
                
            }
        }     
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {

        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerQuitEvent(event.getPlayer())) {

                return;

            }
        }
    }

   @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        
        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerQuitEvent(event.getPlayer())) {

                return;

            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        
        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerInteractEvent(event)) {

                return;

            }
        }
    }
}
