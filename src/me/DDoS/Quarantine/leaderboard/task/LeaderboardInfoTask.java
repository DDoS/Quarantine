package me.DDoS.Quarantine.leaderboard.task;

import java.util.Queue;
import java.util.TimerTask;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.query.Query;

/**
 *
 * @author DDoS
 */
public class LeaderboardInfoTask extends TimerTask {

    private final Leaderboard leaderboard;

    public LeaderboardInfoTask(Leaderboard leaderboard) {

        this.leaderboard = leaderboard;

    }

    @Override
    public void run() {

        if (!leaderboard.getLeaderBoardDB().hasConnection()) {
            
            return;
            
        }
        
        final Queue<Query> queue = leaderboard.getInfoQueries();

        while (!queue.isEmpty()) {

            queue.poll().execute();

        }
    }
}
