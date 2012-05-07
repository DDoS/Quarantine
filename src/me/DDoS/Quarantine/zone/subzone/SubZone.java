package me.DDoS.Quarantine.zone.subzone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import me.DDoS.Quarantine.zone.location.SpawnLocation;
import me.DDoS.Quarantine.zone.region.SpawnRegion;

/**
 *
 * @author DDoS
 */
public class SubZone {

    protected static final Random random = new Random();
    //
    private final SpawnRegion region;
    private final int numOfMobs;
    private final List<EntityType> creatureTypes;
    //
    protected final List<LivingEntity> entities = new ArrayList<LivingEntity>();
    private final List<SpawnLocation> spawnLocs = new ArrayList<SpawnLocation>();

    public SubZone(SpawnRegion region, int numOfMobs, List<EntityType> creatureTypes) {

        this.region = region;
        this.numOfMobs = numOfMobs;
        this.creatureTypes = creatureTypes;

    }

    public boolean hasMobs() {

        return !entities.isEmpty();

    }

    public boolean hasMob(LivingEntity ent) {

        return entities.contains(ent);

    }

    private void generateSpawnLocations() {

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

    public void spawnMobs(int quantity) {

        if (spawnLocs.isEmpty()) {

            generateSpawnLocations();

        }

        if (!spawnLocs.isEmpty()) {

            for (int i = 0; i < quantity; i++) {

                entities.add(spawnLocs.get(random.nextInt(spawnLocs.size())).spawnCreature());

            }
        }
    }

    public void refreshMob(LivingEntity entity) {

        entities.remove(entity);
        entity.remove();
        spawnMobs(1);

    }

    public void removeAllMobs() {
        
        for (LivingEntity entity : entities) {

            entity.remove();

        }

        entities.clear();
        spawnLocs.clear();

    }

    public void checkForDeadMobs() {

        final List<LivingEntity> flagged = new LinkedList<LivingEntity>();

        for (LivingEntity entity : entities) {

            if (entity.isDead()) {

                flagged.add(entity);

            }
        }

        entities.removeAll(flagged);
        spawnMobs(flagged.size());

    }
}