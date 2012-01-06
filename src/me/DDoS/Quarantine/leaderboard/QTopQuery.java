package me.DDoS.Quarantine.leaderboard;

import com.agoragames.leaderboard.LeaderData;
import com.agoragames.leaderboard.Leaderboard;
import java.util.ArrayList;
import java.util.List;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class QTopQuery implements QQuery {

    private Player player;

    public QTopQuery(Player player) {
    
        this.player = player;
    
    }
    
    @Override
    public void execute(Leaderboard lb) {
        
        List<String> top = new ArrayList();

        try {

            List<LeaderData> lds = lb.leadersIn(1, false);

            for (LeaderData ld : lds) {

                top.add(ld.getRank() + ": " + ld.getMember() + " | " + (int) ld.getScore());

            }

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server: " + e.getMessage());
            top.add(ChatColor.RED + "Couldn't connect to leaderboard database. Please inform your operator.");

        }

       for (String t : top) {

            QUtil.tell(player, t);

        } 
    }
}
