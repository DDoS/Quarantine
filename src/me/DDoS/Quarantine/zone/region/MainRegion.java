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
public class MainRegion {

    private final BlockLocation max;
    private final BlockLocation min;
    private final World world;

    public MainRegion(World world, BlockVector pos1, BlockVector pos2) {

        this.max = new BlockLocation(world,
                Math.max(pos1.getBlockX(), pos2.getBlockX()),
                Math.max(pos1.getBlockY(), pos2.getBlockY()),
                Math.max(pos1.getBlockZ(), pos2.getBlockZ()));

        this.min = new BlockLocation(world,
                Math.min(pos1.getBlockX(), pos2.getBlockX()),
                Math.min(pos1.getBlockY(), pos2.getBlockY()),
                Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
        
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
