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
        List<LeaderData> lds = leaderboard.getLeaderBoard().getLeaders(page);

        if (lds.isEmpty()) {

            top.add("No results to list.");

        } else {

            for (LeaderData ld : lds) {

                top.add(ld.getRank() + ": " + ld.getMember() + " | " + ld.getScore());

            }
        }

        leaderboard.addResult(new TopResult(player, top));

    }
}
