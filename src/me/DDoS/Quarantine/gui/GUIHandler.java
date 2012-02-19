package me.DDoS.Quarantine.gui;

import java.util.List;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public interface GUIHandler {

    public void handleZoneList(Player player);

    public void handlePlayerList(Player player, Zone zone);
    
    public void handleTopResults(Player player, List<String> results);
    
}
