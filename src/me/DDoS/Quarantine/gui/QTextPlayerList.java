package me.DDoS.Quarantine.gui;

import me.DDoS.Quarantine.player.QPlayer;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QTextPlayerList {
    
    public void display(Player player, QZone zone) {
        
        QUtil.tell(player, "Players:");
        
        for (QPlayer p : zone.getPlayers()) {
            
            QUtil.tell(player, p.getPlayer().getDisplayName() + ChatColor.GRAY + ", score: " + p.getScore());
            
        }
    } 
}
