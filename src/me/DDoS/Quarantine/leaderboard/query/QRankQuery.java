package me.DDoS.Quarantine.leaderboard.query;

import com.agoragames.leaderboard.Leaderboard;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.QLeaderboard;
import me.DDoS.Quarantine.leaderboard.result.QRankResult;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class QRankQuery implements QQuery {

    private final QLeaderboard leaderboard;
    private final Player player;

    public QRankQuery(QLeaderboard leaderboard, Player player) {

        this.leaderboard = leaderboard;
        this.player = player;

    }

    @Override
    public void execute() {

        String rank;

        try {

            String playerName = player.getName();
            Leaderboard lb = leaderboard.getLeaderBoard();

            if (lb.checkMember(playerName)) {

                rank = lb.rankFor(playerName, false) + ": " + playerName + " | " + (int) lb.scoreFor(playerName);

            } else {

                rank = "You score has yet to be compiled. Please try again later.";

            }

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server: " + e.getMessage());
            rank = ChatColor.RED + "Couldn't connect to leaderboard database. Please inform your operator.";

        }

        leaderboard.addResult(new QRankResult(player, rank));

    }
}
