package me.DDoS.Quarantine.zone.region.provider;

import couk.Adamki11s.Regios.API.RegiosAPI;
import me.DDoS.Quarantine.zone.location.BlockLocation;
import me.DDoS.Quarantine.zone.region.Region;
import me.DDoS.Quarantine.zone.region.SpawnRegion;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author DDoS
 */
public class RegiosRegionProvider implements RegionProvider {

    private final RegiosAPI regios = new RegiosAPI();

    @Override
    public String getName() {
        
        return "Regios";
        
    }
    
    @Override
    public Region getRegion(World world, String regionName) {

        couk.Adamki11s.Regios.Regions.Region region = regios.getRegion(regionName);
        
        if (region == null) {
          
            return null;
        
        }

        Location L1 = region.getL1();
        Location L2 = region.getL2();
        
        BlockLocation max = new BlockLocation(world,
                (int) Math.max(L1.getX(), L2.getX()),
                (int) Math.max(L1.getY(), L2.getY()),
                (int) Math.max(L1.getZ(), L2.getZ()));

        BlockLocation min = new BlockLocation(world,
                (int) Math.min(L1.getX(), L2.getX()),
                (int) Math.min(L1.getY(), L2.getY()),
                (int) Math.min(L1.getZ(), L2.getZ()));

        return new Region(world, max, min);

    }

    @Override
    public SpawnRegion getSpawnRegion(World world, String regionName) {

        couk.Adamki11s.Regios.Regions.Region region = regios.getRegion(regionName);
        
        if (region == null) {
          
            return null;
        
        }
        
        Location L1 = region.getL1();
        Location L2 = region.getL2();
        
        BlockLocation max = new BlockLocation(world,
                (int) Math.max(L1.getX(), L2.getX()),
                (int) Math.max(L1.getY(), L2.getY()),
                (int) Math.max(L1.getZ(), L2.getZ()));

        BlockLocation min = new BlockLocation(world,
                (int) Math.min(L1.getX(), L2.getX()),
                (int) Math.min(L1.getY(), L2.getY()),
                (int) Math.min(L1.getZ(), L2.getZ()));

        return new SpawnRegion(world, max, min);

    }
}
