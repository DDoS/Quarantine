package me.DDoS.Quarantine.leaderboard.task;

import java.util.Queue;
import java.util.TimerTask;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.QLeaderboard;
import me.DDoS.Quarantine.leaderboard.QScoreUpdate;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class QScoreUpdateTask extends TimerTask {

    private final QLeaderboard leaderboard;

    public QScoreUpdateTask(QLeaderboard leaderboard) {

        this.leaderboard = leaderboard;

    }

    @Override
    public void run() {
    
        final Queue<QScoreUpdate> queue = leaderboard.getUpdates();

        try {

            while (!queue.isEmpty()) {

                QScoreUpdate update = queue.poll();
                leaderboard.getLeaderBoard().rankMember(update.getPlayer(), update.getScore());

            }

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server. Is it on?");
            Quarantine.log.info("[Quarantine] Error message: " + e.getMessage());

        }
    }
}
