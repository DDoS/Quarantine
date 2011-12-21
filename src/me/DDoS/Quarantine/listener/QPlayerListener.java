package me.DDoS.Quarantine.listener;

import java.util.Collection;
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

        Collection<QZone> zones = plugin.getZones();
        
        for (QZone zone : zones) {

            if (zone.passPlayerTeleportEvent(event)) {
                
                return;
                
            }
        }
    }
    
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        
        Collection<QZone> zones = plugin.getZones();
        
        for (QZone zone : zones) {

            if (zone.passPlayerRespawnEvent(event, plugin)) {
                
                return;
                
            }
        }     
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

        Collection<QZone> zones = plugin.getZones();
        
        for (QZone zone : zones) {

            if (zone.leavePlayer(event.getPlayer())) {

                return;

            }
        }
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {

        Collection<QZone> zones = plugin.getZones();
        
        for (QZone zone : zones) {

            if (zone.leavePlayer(event.getPlayer())) {

                return;

            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

        Collection<QZone> zones = plugin.getZones();
        
        for (QZone zone : zones) {

            if (zone.passPlayerInteractEvent(event)) {

                return;

            }
        }
    }
}