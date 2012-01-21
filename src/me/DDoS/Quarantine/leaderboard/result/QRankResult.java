package me.DDoS.Quarantine.leaderboard.result;

import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QRankResult implements QResult{
    
    private final Player player;
    private final String result;

    public QRankResult(Player player, String result) {
        
        this.player = player;
        this.result = result;
    
    }

    @Override
    public void display() {

        QUtil.tell(player, result);
        
    }   
}
