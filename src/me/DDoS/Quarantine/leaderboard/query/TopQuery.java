package me.DDoS.Quarantine.leaderboard.query;

import me.DDoS.Quarantine.leaderboard.LeaderData;
import java.util.ArrayList;
import java.util.List;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.result.TopResult;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class TopQuery implements Query {

    private final Leaderboard leaderboard;
    private final Player player;
    private final int page;

    public TopQuery(Leaderboard leaderboard, Player player, int page) {

        this.leaderboard = leaderboard;
        this.player = player;
        this.page = page;
        
    }

    @Override
    public void execute() {

        List<String> top = new ArrayList();

        try {

            List<LeaderData> lds = leaderboard.getLeaderBoard().getLeaders(page);

            for (LeaderData ld : lds) {

                top.add(ld.getRank() + ": " + ld.getMember() + " | " + (int) ld.getScore());

            }

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server: " + e.getMessage());
            top.add(ChatColor.RED + "Couldn't connect to leaderboard database. Please inform your operator.");

        }
        
        leaderboard.addResult(new TopResult(player, top));
        
    }
}
