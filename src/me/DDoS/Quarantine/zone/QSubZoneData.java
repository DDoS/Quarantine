package me.DDoS.Quarantine.zone;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.CreatureType;

/**
 *
 * @author DDoS
 */
public class QSubZoneData {
    
    private int numOfMobs;
    private List<String> mobTypes;
    
    public QSubZoneData(int numOfMobs, List<String> mobTypes) {
        
        this.numOfMobs = numOfMobs;
        this.mobTypes = mobTypes;
        
    }
    
    public List<CreatureType> getMobTypes() {
        
        List<CreatureType> types = new ArrayList<CreatureType>();
        
        for (String mobType : mobTypes) {
            
            types.add(CreatureType.fromName(mobType));
            
        }
        
        return types;
        
    }
    
    public int getNumberOfMobs() {
        
        return numOfMobs;
        
    }
}
