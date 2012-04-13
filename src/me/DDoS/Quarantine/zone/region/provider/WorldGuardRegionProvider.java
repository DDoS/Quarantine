package me.DDoS.Quarantine.zone.region.provider;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.GlobalRegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.DDoS.Quarantine.zone.location.BlockLocation;
import me.DDoS.Quarantine.zone.region.Region;
import me.DDoS.Quarantine.zone.region.SpawnRegion;
import org.bukkit.World;

/**
 *
 * @author DDoS
 */
public class WorldGuardRegionProvider implements RegionProvider {

    private final GlobalRegionManager regionManager;

    @Override
    public String getName() {
        
        return "WorldGuard";
        
    }
    
    public WorldGuardRegionProvider(GlobalRegionManager regionManager) {
        
        this.regionManager = regionManager;
    
    }
    
    @Override
    public Region getRegion(World world, String regionName) {
        
        ProtectedRegion region = regionManager.get(world).getRegion(regionName);
        Vector vectorMax = region.getMaximumPoint();
        Vector vectorMin = region.getMinimumPoint();
        BlockLocation max = new BlockLocation(world, vectorMax.getBlockX(), vectorMax.getBlockY(), vectorMax.getBlockZ());
        BlockLocation min = new BlockLocation(world, vectorMin.getBlockX(), vectorMin.getBlockY(), vectorMin.getBlockZ());
        return new Region(world, max, min);
        
    }

    @Override
    public SpawnRegion getSpawnRegion(World world, String regionName) {
        
        ProtectedRegion region = regionManager.get(world).getRegion(regionName);
        Vector vectorMax = region.getMaximumPoint();
        Vector vectorMin = region.getMinimumPoint();
        BlockLocation max = new BlockLocation(world, vectorMax.getBlockX(), vectorMax.getBlockY(), vectorMax.getBlockZ());
        BlockLocation min = new BlockLocation(world, vectorMin.getBlockX(), vectorMin.getBlockY(), vectorMin.getBlockZ());
        return new SpawnRegion(world, max, min);
        
    } 
}
