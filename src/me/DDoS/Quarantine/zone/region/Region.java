package me.DDoS.Quarantine.zone.region;

import com.sk89q.worldedit.BlockVector;
import me.DDoS.Quarantine.zone.location.BlockLocation;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author DDoS 
 * 
 */
public class Region {

    protected final BlockLocation max;
    protected final BlockLocation min;
    protected final World world;

    public Region(World world, BlockLocation max, BlockLocation min) {

        this.max = max;
        this.min = min;
        this.world = world;

    }

    public boolean containsLocation(Location loc) {

        if (!loc.getWorld().equals(world)) {
            
            return false;
            
        }
        
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        return (x >= min.getX() && x <= max.getX()
                && y >= min.getY() && y <= max.getY()
                && z >= min.getZ() && z <= max.getZ());

    }
    
    public boolean containsChunk(Chunk chunk) {

        if (!chunk.getWorld().equals(world)) {
            
            return false;
            
        }
        
        int minChunkX = min.getX() >> 4;
        int minChunkZ = min.getZ() >> 4;
        
        int maxChunkX = max.getX() >> 4;
        int maxChunkZ = max.getZ() >> 4;
        
        return (chunk.getX() >= minChunkX && chunk.getX() <= maxChunkX)
                && (chunk.getZ() >= minChunkZ && chunk.getZ() <= maxChunkZ);

    }  
}
