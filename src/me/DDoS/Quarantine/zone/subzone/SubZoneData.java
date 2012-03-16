package me.DDoS.Quarantine.zone.subzone;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.EntityType;

/**
 *
 * @author DDoS
 */
public class SubZoneData {
    
    private final int numOfMobs;
    private final List<String> mobTypes;
    
    public SubZoneData(int numOfMobs, List<String> mobTypes) {
        
        this.numOfMobs = numOfMobs;
        this.mobTypes = mobTypes;
        
    }
    
    public List<EntityType> getMobTypes() {
        
        List<EntityType> types = new ArrayList<EntityType>();
        
        for (String mobType : mobTypes) {
            
            types.add(EntityType.fromName(mobType));
            
        }
        
        return types;
        
    }
    
    public int getNumberOfMobs() {
        
        return numOfMobs;
        
    }
}
