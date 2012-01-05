package me.DDoS.Quarantine.leaderboard;

import com.agoragames.leaderboard.LeaderData;
import com.agoragames.leaderboard.Leaderboard;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.QPlayer;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author DDoS
 */
public class QLeaderboard {

    public static String HOST;
    public static int PORT;
    public static boolean USE = false;
    //
    private Leaderboard lb;
    //
    private final Timer timer = new Timer();
    private final Map<QPlayer, QScoreUpdate> updates = new ConcurrentHashMap<QPlayer, QScoreUpdate>();

    public QLeaderboard(String zoneName) {

        lb = new Leaderboard(zoneName, HOST, PORT, 5);
        timer.scheduleAtFixedRate(new QScoreUpdateTask(this), 20000L, 20000L);

    }

    public Leaderboard getLeaderBoard() {

        return lb;

    }
   
    public void queueScoreUpdate(QPlayer player) {
        
        updates.put(player, new QScoreUpdate(player.getPlayer().getName(), player.getScore()));
        
    }
    
    public Queue<QScoreUpdate> getQueue() {
        
        final Queue<QScoreUpdate> queue = new ConcurrentLinkedQueue<QScoreUpdate>();
        queue.addAll(updates.values());
        updates.clear();
        return queue;

    }

    public String getScoreAndRank(String playerName) {

        try {

            return lb.rankFor(playerName, false) + ": " + playerName + ", " + (int) lb.scoreFor(playerName);

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server. Is it on?");
            return "Couldn't connect to leaderboard database. Please inform your operator.";

        }
    }

    public List<String> getTopFive() {

        List<String> top = new ArrayList();

        try {

            List<LeaderData> lds = lb.leadersIn(1, false);

            for (LeaderData ld : lds) {

                top.add(ld.getRank() + ": " + ld.getMember() + ", " + (int) ld.getScore());

            }

        } catch (JedisConnectionException e) {

            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server. Is it on?");
            top.add("Couldn't connect to leaderboard database. Please inform your operator.");

        }

        return top;

    }

    public void disconnect() {

        timer.cancel();
        lb.disconnect();

    }
}
