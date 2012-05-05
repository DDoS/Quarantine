package me.DDoS.Quarantine.leaderboard.query;

import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.LeaderboardDB;
import me.DDoS.Quarantine.leaderboard.result.RankResult;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
        LeaderboardDB lb = leaderboard.getLeaderBoardDB();

        if (lb.isMember(playerName)) {

            rank = lb.getRank(playerName) + ": " + playerName + " | " + lb.getScore(playerName);

        } else {

            rank = ChatColor.YELLOW + "Your score has yet to be compiled. Please try again later.";

        }

        leaderboard.addResult(new RankResult(player, rank));

    }
}
