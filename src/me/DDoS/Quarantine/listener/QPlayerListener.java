package me.DDoS.Quarantine.listener;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author DDoS
 */
public class QPlayerListener extends PlayerListener {

    private Quarantine plugin;

    public QPlayerListener(Quarantine plugin) {

        this.plugin = plugin;

    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        
        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerTeleportEvent(event)) {
                
                return;
                
            }
        }
    }
    
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        
        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerRespawnEvent(event, plugin)) {
                
                return;
                
            }
        }     
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerQuitEvent(event.getPlayer())) {

                return;

            }
        }
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        
        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerQuitEvent(event.getPlayer())) {

                return;

            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        
        for (QZone zone : plugin.getZones()) {

            if (zone.passPlayerInteractEvent(event)) {

                return;

            }
        }
    }
}