package me.DDoS.Quarantine.util;

/**
 *
 * @author DDoS
 */
import java.io.Serializable;

public class QInventoryItem implements Serializable {
    
    private int typeId;
    private int amount;
    private short durability;
    
    public QInventoryItem(int typeId, int amount, short durability) {
        
        this.typeId = typeId;
        this.amount = amount;
        this.durability = durability;
        
    }

    public int getTypeId() { 
        
        return typeId;
        
    }
    
    public int getAmount() { 
        
        return amount; 
    
    }
    
    public short getDurability() {
        
        return durability;
    
    }
}
