package me.DDoS.Quarantine.leaderboard.query;

import com.agoragames.leaderboard.LeaderData;
import java.util.ArrayList;
import java.util.List;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.QLeaderboard;
import me.DDoS.Quarantine.leaderboard.result.QTopResult;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class QTopQuery implements QQuery {

    private final QLeaderboard leaderboard;
    private final Player player;
    private final String playerName;

    public QTopQuery(QLeaderboard leaderboard, Player player) {

        this.leaderboard = leaderboard;
        this.player = player;
        this.playerName = player.getName();
        
    }

    @Override
    public void execute() {

        List<String> top = new ArrayList();

        try {

            List<LeaderData> lds = leaderboard.getLeaderBoard().leadersIn(1, false);

            for (LeaderData ld : lds) {

                top.add(ld.getRank() + ": " + ld.getMember() + " | " + (int) ld.getScore());

            }

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server: " + e.getMessage());
            top.add(ChatColor.RED + "Couldn't connect to leaderboard database. Please inform your operator.");

        }
        
        leaderboard.addResult(new QTopResult(player, top));
        
    }
}