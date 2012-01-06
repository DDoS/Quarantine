package me.DDoS.Quarantine.leaderboard;

import com.agoragames.leaderboard.Leaderboard;
import java.util.Queue;
import java.util.TimerTask;
import me.DDoS.Quarantine.Quarantine;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class QLeaderboardInfoTask extends TimerTask {

    private QLeaderboard leaderboard;

    public QLeaderboardInfoTask(QLeaderboard leaderboard) {

        this.leaderboard = leaderboard;

    }

    @Override
    public void run() {

        final Queue<QQuery> queue = leaderboard.getLeaderboardInfoQueries();

        try {

            Leaderboard lb = leaderboard.getLeaderBoard();

            while (!queue.isEmpty()) {

                queue.poll().execute(lb);

            }

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server. Is it on?");
            Quarantine.log.info("[Quarantine] Error message: " + e.getMessage());

        }
    }
}
