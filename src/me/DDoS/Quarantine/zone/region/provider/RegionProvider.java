package me.DDoS.Quarantine.zone.region.provider;

import me.DDoS.Quarantine.zone.region.Region;
import me.DDoS.Quarantine.zone.region.SpawnRegion;
import org.bukkit.World;


/**
 *
 * @author DDoS
 */
public interface RegionProvider {
    
    public String getName();
    
    public Region getRegion(World world, String regionName);
    
    public SpawnRegion getSpawnRegion(World world, String regionName);
    
}
