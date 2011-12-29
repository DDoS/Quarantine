package me.DDoS.Quarantine.gui;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QTextZoneList {

    private Quarantine plugin;

    public QTextZoneList(Quarantine plugin) {
        
        this.plugin = plugin;
    
    }
    
    public void display(Player player) {
        
        QUtil.tell(player, "Zones:");
        
        for (QZone zone : plugin.getZones()) {
            
            QUtil.tell(player, zone.getName()
                    + ": " + zone.getNumOfPlayers()
                    + "/" + zone.getMaxNumOfPlayers());
            
        }
    } 
}
