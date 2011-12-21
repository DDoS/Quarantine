package me.DDoS.Quarantine.zone;

import me.DDoS.Quarantine.QSpawnLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author DDoS
 */
public class QSubZone {

    private QSubRegion region;
    private int numOfMobs;
    private List<CreatureType> mobTypes;
    //
    private final List<LivingEntity> entities = new ArrayList<LivingEntity>();
    private final List<QSpawnLocation> spawnLocs = new ArrayList<QSpawnLocation>();

    public QSubZone(QSubRegion region, int numOfMobs, List<CreatureType> mobTypes) {

        this.region = region;
        this.numOfMobs = numOfMobs;
        this.mobTypes = mobTypes;

    }

    public boolean hasMobs() {

        return !entities.isEmpty();

    }

    public void generateSpawnLocations() {

        Random rand = new Random();
        Iterator<Location> iter = region.spawnLocationIterator();

        while (iter.hasNext()) {

            Location loc = (Location) iter.next();
            
            if (loc != null) {
            
                CreatureType type = mobTypes.get(rand.nextInt(mobTypes.size()));
                spawnLocs.add(new QSpawnLocation(loc, type));
            
            }
        }
    }

    public void spawnStartingMobs() {

        spawnMobs(numOfMobs);

    }

    public void spawnMobs(int numToSpawn) {

        if (spawnLocs.isEmpty()) {

            generateSpawnLocations();

        }

        Random rand = new Random();

        for (int i = 0; i < numToSpawn; i++) {

            int randIndex = rand.nextInt(spawnLocs.size());
            entities.add(spawnLocs.get(randIndex).spawnMob());

        }
    }

    public boolean removeAndSpawnNewEntity(LivingEntity entity) {

        if (entities.contains(entity)) {

            entities.remove(entity);
            entity.remove();
            spawnMobs(1);
            return true;

        }

        return false;

    }

    public void removeAllMobs() {

        for (LivingEntity entity : entities) {

            entity.remove();

        }

        entities.clear();
        spawnLocs.clear();

    }
    
    public boolean containsMob(LivingEntity ent) {
        
        return entities.contains(ent);
 
    }
    
    public void checkForDeadMobs() {
        
        List<LivingEntity> flagged = new ArrayList<LivingEntity>();
        
        for (LivingEntity ent : entities) {
            
            if (ent.isDead()) {
                
                flagged.add(ent);
            
            }
        }

        entities.removeAll(flagged);
        spawnMobs(flagged.size());
        
    }
}