package me.DDoS.Quarantine.zone;

import com.sk89q.worldedit.Vector;
import java.util.Iterator;
import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author DDoS 
 * 
 */
public class QSubRegion {

    private Vector max;
    private Vector min;
    private World world;

    public QSubRegion(Vector pos1, Vector pos2, World world) {

        this.max = new Vector(Math.max(pos1.getX(), pos2.getX()),
                Math.max(pos1.getY(), pos2.getY()),
                Math.max(pos1.getZ(), pos2.getZ()));

        this.min = new Vector(Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ()));

        this.world = world;

    }

    public Iterator<Location> spawnLocationIterator() {

        return new Iterator<Location>() {

            private int nextX = min.getBlockX();
            private int nextY = min.getBlockY();
            private int nextZ = min.getBlockZ();

            @Override
            public boolean hasNext() {

                return (nextX != Integer.MIN_VALUE);

            }

            @Override
            public Location next() {

                if (!hasNext()) {

                    throw new java.util.NoSuchElementException();

                }

                Location answer = new Location(world, nextX, nextY, nextZ);

                while (!isSpawnable(answer)) {

                    increment();

                    if (nextX == Integer.MIN_VALUE) {

                        return null;

                    }

                    answer = new Location(world, nextX, nextY, nextZ);

                }

                increment();
                return answer;

            }

            private void increment() {

                if (++nextX > max.getBlockX()) {

                    nextX = min.getBlockX();

                    if (++nextY > max.getBlockY()) {

                        nextY = min.getBlockY();

                        if (++nextZ > max.getBlockZ()) {

                            nextX = Integer.MIN_VALUE;

                        }
                    }
                }
            }

            private boolean isSpawnable(Location loc) {

                Block block = loc.getBlock();

                return ((block.getType().equals(Material.AIR) || block.getType().equals(Material.SNOW))
                        && QUtil.acceptsMobs(block.getRelative(BlockFace.DOWN))
                        && block.getRelative(BlockFace.UP).getType().equals(Material.AIR));

            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();

            }
        };
    }
}