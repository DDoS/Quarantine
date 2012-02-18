package me.DDoS.Quarantine.leaderboard.query;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.LeaderboardDB;
import me.DDoS.Quarantine.leaderboard.result.RankResult;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class RankQuery implements Query {

    private final Leaderboard leaderboard;
    private final Player player;
    private final String playerName;

    public RankQuery(Leaderboard leaderboard, Player player) {

        this.leaderboard = leaderboard;
        this.player = player;
        this.playerName = player.getName();
        
    }

    @Override
    public void execute() {

        String rank;

        try {

            LeaderboardDB lb = leaderboard.getLeaderBoard();

            if (lb.isMember(playerName)) {

                rank = lb.getRank(playerName) + ": " + playerName + " | " + lb.getScore(playerName);

            } else {

                rank = "You score has yet to be compiled. Please try again later.";

            }

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server: " + e.getMessage());
            rank = ChatColor.RED + "Couldn't connect to leaderboard database. Please inform your operator.";

        }

        leaderboard.addResult(new RankResult(player, rank));

    }
}
