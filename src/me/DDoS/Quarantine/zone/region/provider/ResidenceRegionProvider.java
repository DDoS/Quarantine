package me.DDoS.Quarantine.zone.region.provider;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import me.DDoS.Quarantine.zone.location.BlockLocation;
import me.DDoS.Quarantine.zone.region.Region;
import me.DDoS.Quarantine.zone.region.SpawnRegion;
import org.bukkit.World;

/**
 *
 * @author DDoS
 */
public class ResidenceRegionProvider implements RegionProvider {

    
    @Override
    public String getName() {
        
        return "Residence";
        
    }
    
    @Override
    public Region getRegion(World world, String regionName) {

        ClaimedResidence residence = Residence.getResidenceManager().getByName(regionName);
        
        if (residence == null) {
            
            return null;
            
        }
        
        CuboidArea region = residence.getArea("main");
        return new Region(world, new BlockLocation(region.getHighLoc()), new BlockLocation(region.getLowLoc()));
        
    }

    @Override
    public SpawnRegion getSpawnRegion(World world, String regionNames) {
        
        String[] names = regionNames.split(":");
        ClaimedResidence residence = Residence.getResidenceManager().getByName(names[0]);
        
        if (residence == null) {
            
            return null;
            
        }
        
        ClaimedResidence subRegion = residence.getSubzone(names[1]);
        
        if (subRegion == null) {
            
            return null;
            
        }
        
        CuboidArea area = subRegion.getArea(names[1]);      
        return new SpawnRegion(world, new BlockLocation(area.getHighLoc()), new BlockLocation(area.getLowLoc()));
        
    }
}
