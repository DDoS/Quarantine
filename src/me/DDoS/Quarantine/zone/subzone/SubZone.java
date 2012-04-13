package me.DDoS.Quarantine.zone.subzone;

import me.DDoS.Quarantine.zone.location.SpawnLocation;
import me.DDoS.Quarantine.zone.region.SpawnRegion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author DDoS
 */
public class SubZone {

    private final SpawnRegion region;
    private final int numOfMobs;
    private final List<EntityType> creatureTypes;
    private final boolean softRespawn;
    //
    private final List<LivingEntity> entities = new ArrayList<LivingEntity>();
    private final List<SpawnLocation> spawnLocs = new ArrayList<SpawnLocation>();

    public SubZone(SpawnRegion region, int numOfMobs, boolean softRespawn, List<EntityType> creatureTypes) {

        this.softRespawn = softRespawn;
        this.region = region;
        this.numOfMobs = numOfMobs;
        this.creatureTypes = creatureTypes;

    }

    public boolean hasMobs() {

        return !entities.isEmpty();

    }

    public void generateSpawnLocations() {

        Random rand = new Random();
        Iterator<SpawnLocation> iter = region.spawnLocationIterator();

        while (iter.hasNext()) {

            SpawnLocation loc = iter.next();

            if (loc != null) {

                loc.setCreatureType(creatureTypes.get(rand.nextInt(creatureTypes.size())));
                spawnLocs.add(loc);

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

        if (!spawnLocs.isEmpty()) {

            Random rand = new Random();

            for (int i = 0; i < numToSpawn; i++) {

                entities.add(spawnLocs.get(rand.nextInt(spawnLocs.size())).spawnCreature());

            }
        }
    }

    public boolean removeAndSpawnNewEntity(LivingEntity entity) {

        if (!softRespawn) {

            if (entities.contains(entity)) {

                entities.remove(entity);
                entity.remove();
                spawnMobs(1);
                return true;

            }

            return false;

        } else {

            return entities.contains(entity);

        }
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