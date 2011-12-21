package me.DDoS.Quarantine.zone;

import com.sk89q.worldedit.Vector;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author DDoS 
 * 
 */
public class QMainRegion {

    private Vector max;
    private Vector min;
    private World world;

    public QMainRegion(Vector pos1, Vector pos2, World world) {

        this.max = new Vector(Math.max(pos1.getX(), pos2.getX()),
                Math.max(pos1.getY(), pos2.getY()),
                Math.max(pos1.getZ(), pos2.getZ()));

        this.min = new Vector(Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ()));
        
        this.world = world;

    }

    public boolean containsLocation(Location loc) {

        if (!loc.getWorld().equals(world)) {
            
            return false;
            
        }
        
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        return (x >= min.getBlockX() && x <= max.getBlockX()
                && y >= min.getBlockY() && y <= max.getBlockY()
                && z >= min.getBlockZ() && z <= max.getBlockZ());

    }
    
    public boolean containsChunk(Chunk chunk) {

        if (!chunk.getWorld().equals(world)) {
            
            return false;
            
        }
        
        int minChunkX = min.getBlockX() >> 4;
        int minChunkZ = min.getBlockZ() >> 4;
        
        int maxChunkX = max.getBlockX() >> 4;
        int maxChunkZ = max.getBlockZ() >> 4;
        
        return ((chunk.getX() >= minChunkX && chunk.getX() <= maxChunkX)
                && (chunk.getZ() >= minChunkZ && chunk.getZ() <= maxChunkZ));

    }  
}