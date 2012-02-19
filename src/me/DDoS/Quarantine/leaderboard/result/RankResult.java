package me.DDoS.Quarantine.leaderboard.result;

import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class RankResult implements Result {
    
    private final Player player;
    private final String result;

    public RankResult(Player player, String result) {
        
        this.player = player;
        this.result = result;
    
    }

    @Override
    public void display() {

        QUtil.tell(player, result);
        
    }   
}
