package me.DDoS.Quarantine.zone.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author DDoS
 */
public class BlockLocation {

    protected final int x, y, z;
    protected final World world;

    public BlockLocation(World world, int x, int y, int z) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

    }

    public BlockLocation(Location loc) {

        this.world = loc.getWorld();
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();

    }

    public World getWorld() {

        return world;

    }

    public int getX() {

        return x;

    }

    public int getY() {

        return y;

    }

    public int getZ() {

        return z;

    }
    
    public Block getBlock() {
        
        return world.getBlockAt(x, y, z);
        
    }

    @Override
    public String toString() {

        return "world: " + world.getName()
                + ", X: " + x
                + ", Y: " + y
                + ", Z: " + z;

    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {

            return true;

        }

        if (other instanceof Location) {

            Location otherLoc = (Location) other;

            return world == otherLoc.getWorld()
                    && x == otherLoc.getBlockX()
                    && y == otherLoc.getBlockY()
                    && z == otherLoc.getBlockZ();

        } else if (other instanceof BlockLocation) {
            
            BlockLocation otherLoc = (BlockLocation) other;

            return world == otherLoc.getWorld()
                    && x == otherLoc.getX()
                    && y == otherLoc.getY()
                    && z == otherLoc.getZ();
            
        } else {
            
            return false;
            
        }
    }

    @Override
    public int hashCode() {
        
        int hash = 5;
        
        hash = 83 * hash + this.x;
        hash = 83 * hash + this.y;
        hash = 83 * hash + this.z;
        hash = 83 * hash + (this.world != null ? this.world.hashCode() : 0);
        
        return hash;
    
    }
}
