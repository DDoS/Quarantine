package me.DDoS.Quarantine.zone.spawning;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author DDoS
 */
public class QSpawnLocation extends Location {
    
    private final CreatureType mobType;
    
    public QSpawnLocation(Location location, CreatureType mobType) {
        
        super(location.getWorld(), location.getX(), location.getY(), location.getZ());
        
        this.mobType = mobType;
        
    }
    
    public LivingEntity spawnMob() {

        return getWorld().spawnCreature(this, mobType);
        
    }
}