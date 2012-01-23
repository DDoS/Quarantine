package me.DDoS.Quarantine.gui;

import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public interface QGUIHandler {

    public void handleZoneList(Player player);

    public void handlePlayerList(Player player, QZone zone);
    
}
