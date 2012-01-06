package me.DDoS.Quarantine.leaderboard;

import com.agoragames.leaderboard.Leaderboard;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class QRankQuery implements QQuery {

    private Player player;

    public QRankQuery(Player player) {
        
        this.player = player;
    
    }
    
    @Override
    public void execute(Leaderboard lb) {
        
        String rank = "You score has yet to be compiled. Please try again later.";
        
        try {

            String playerName = player.getName();
            
            if (lb.checkMember(playerName)) {
            
                    rank = lb.rankFor(playerName, false) + ": " + playerName + " | " + (int) lb.scoreFor(playerName);

            }
            
        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server: " + e.getMessage());
            rank = ChatColor.RED + "Couldn't connect to leaderboard database. Please inform your operator.";

        } 
        
        QUtil.tell(player, rank);
        
    }
}
