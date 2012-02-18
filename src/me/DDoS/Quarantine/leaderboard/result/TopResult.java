package me.DDoS.Quarantine.leaderboard.result;

import java.util.List;
import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class TopResult implements Result{
    
    private final Player player;
    private final List<String> results;

    public TopResult(Player player, List<String> results) {
        
        this.player = player;      
        this.results = results;
        
    }  
    
    @Override
    public void display() {
        
        for (String result : results) {

            QUtil.tell(player, result);

        }
    }
}
