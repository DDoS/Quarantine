package me.DDoS.Quarantine.zone.subzone;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import me.DDoS.Quarantine.zone.region.SpawnRegion;

/**
 *
 * @author DDoS
 */
public class SoftRespawnSubZone extends SubZone {

    private int mobsToRespawn = 0;

    public SoftRespawnSubZone(SpawnRegion region, int numOfMobs, List<EntityType> creatureTypes) {

        super(region, numOfMobs, creatureTypes);

    }

    @Override
    public void refreshMob(LivingEntity entity) {

        entities.remove(entity);
        entity.remove();
        mobsToRespawn++;

    }

    @Override
    public void checkForDeadMobs() {

        final List<LivingEntity> flagged = new LinkedList<LivingEntity>();

        for (LivingEntity entity : entities) {

            if (entity.isDead()) {

                flagged.add(entity);

            }
        }

        mobsToRespawn += flagged.size();
        entities.removeAll(flagged);
        respawnMobs();

    }

    private void respawnMobs() {

        if (mobsToRespawn == 0) {
            
            return;
            
        }
        
        final int minToRespawn = (int) Math.ceil((float) mobsToRespawn / 2f);  
        final int numToRespawn = random.nextInt(minToRespawn) + minToRespawn;
        spawnMobs(numToRespawn);
        mobsToRespawn -= numToRespawn;

    }
}
