package me.DDoS.Quarantine.zone.task;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.Zone;
import me.DDoS.Quarantine.zone.subzone.SubZone;

/**
 *
 * @author DDoS
 */
public class MobCheckTask implements Runnable {

    private final Zone zone;
    
    public MobCheckTask(Zone zone) {
        
        this.zone = zone;
        
    }
    
    @Override
    public void run() {
        
        for (SubZone subZone : zone.getSubZones()) {

            subZone.checkForDeadMobs();

        }

        Quarantine.log.info("[Quarantine] Finished checking mobs.");

    }
}
