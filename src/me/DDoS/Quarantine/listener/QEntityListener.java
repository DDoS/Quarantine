package me.DDoS.Quarantine.listener;

import java.util.Collection;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

/**
 *
 * @author DDoS
 */
public class QEntityListener extends EntityListener {

    private Quarantine plugin;

    public QEntityListener(Quarantine plugin) {

        this.plugin = plugin;

    }

    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        Collection<QZone> zones = plugin.getZones();
        
        for (QZone zone : zones) {

            if (zone.passCreatureSpawnEvent(event)) {
                
                return;
                
            }
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof LivingEntity) {

            LivingEntity ent = (LivingEntity) event.getEntity();

            if (ent instanceof Player) {

                Player player = (Player) ent;

                Collection<QZone> zones = plugin.getZones();
                
                for (QZone zone : zones) {

                    if (zone.passPlayerDeathEvent(player, event)) {
                        
                        return;
                        
                    }

                }

            } else {

                Collection<QZone> zones = plugin.getZones();
                
                for (QZone zone : zones) {

                    if (zone.passEntityDeathEvent(ent, event)) {
                        
                        return;
                        
                    }
                }
            }
        }
    }

    @Override
    public void onEntityCombust(EntityCombustEvent event) {

        Collection<QZone> zones = plugin.getZones();
        
        for (QZone zone : zones) {

            if (zone.passEntityCombustEvent(event)) {
                
                return;
                
            }
        }
    }
}