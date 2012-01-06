package me.DDoS.Quarantine.leaderboard;

import com.agoragames.leaderboard.Leaderboard;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.DDoS.Quarantine.player.QPlayer;
import org.bukkit.entity.Player;

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
    //
    private final Map<QPlayer, QScoreUpdate> updates = new ConcurrentHashMap<QPlayer, QScoreUpdate>();
    private final Queue<QQuery> queries = new ConcurrentLinkedQueue<QQuery>();

    public QLeaderboard(String zoneName) {

        lb = new Leaderboard(zoneName, HOST, PORT, 5);
        timer.scheduleAtFixedRate(new QScoreUpdateTask(this), 20000L, 20000L);
        timer.scheduleAtFixedRate(new QLeaderboardInfoTask(this), 500L, 500L);

    }

    public synchronized Leaderboard getLeaderBoard() {

        return lb;

    }
   
    public void queueScoreUpdate(QPlayer player) {
        
        updates.put(player, new QScoreUpdate(player.getPlayer().getName(), player.getScore()));
        
    }
    
    public Queue<QScoreUpdate> getUpdateQueue() {
        
        final Queue<QScoreUpdate> queue = new ConcurrentLinkedQueue<QScoreUpdate>();
        queue.addAll(updates.values());
        updates.clear();
        return queue;

    }
    
    public Queue<QQuery> getLeaderboardInfoQueries() {
        
        return queries;
        
    }

    public void addRankQuery(Player player) {

        queries.add(new QRankQuery(player));
        
    }

    public void addTopQuery(Player player) {

        queries.add(new QTopQuery(player));

    }

    public void disconnect() {

        timer.cancel();
        lb.disconnect();

    }
}
