package me.DDoS.Quarantine.leaderboard;

import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.entity.Player;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.gui.GUIHandler;
import me.DDoS.Quarantine.leaderboard.result.Result;
import me.DDoS.Quarantine.leaderboard.mysql.MySQLLeaderboardDB;
import me.DDoS.Quarantine.leaderboard.task.DisplayInfoTask;
import me.DDoS.Quarantine.player.QPlayer;
import me.DDoS.Quarantine.leaderboard.query.Query;
import me.DDoS.Quarantine.leaderboard.query.TopQuery;
import me.DDoS.Quarantine.leaderboard.query.RankQuery;
import me.DDoS.Quarantine.leaderboard.task.ScoreUpdateTask;
import me.DDoS.Quarantine.leaderboard.task.LeaderboardInfoTask;
import me.DDoS.Quarantine.leaderboard.redis.RedisLeaderboardDB;

/**
 *
 * @author DDoS
 */
public class Leaderboard {

    public static boolean ENABLED;
    public static final byte PAGE_SIZE = 5;
    //
    public static String TYPE;
    public static String HOST;
    public static String DB_NAME;
    public static String USER;
    public static String PASSWORD;
    public static int PORT;
    //
    private final Quarantine plugin;
    //
    private final LeaderboardDB lbdb;
    //
    private final Timer timer = new Timer();
    private final int displayInfoTaskID;
    //
    private final Map<QPlayer, ScoreUpdate> updates = new ConcurrentHashMap<QPlayer, ScoreUpdate>();
    //
    private final Queue<Query> queries = new ConcurrentLinkedQueue<Query>();
    //
    private final Queue<Result> results = new ConcurrentLinkedQueue<Result>();
    //
    private volatile int numberOfPages;

    public Leaderboard(Quarantine plugin, String zoneName) {

        this.plugin = plugin;

        if (TYPE.equals("Redis")) {

            lbdb = new RedisLeaderboardDB(zoneName, PAGE_SIZE);

        } else {

            lbdb = new MySQLLeaderboardDB(zoneName, PAGE_SIZE);

        }

        timer.scheduleAtFixedRate(new ScoreUpdateTask(this), 10000L, 10000L);
        timer.scheduleAtFixedRate(new LeaderboardInfoTask(this), 500L, 500L);
        displayInfoTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new DisplayInfoTask(this), 10L, 10L);

    }

    public LeaderboardDB getLeaderBoardDB() {

        return lbdb;

    }

    public void queueScoreUpdate(QPlayer player) {

        updates.put(player, new ScoreUpdate(player.getPlayer().getName(), player.getScore()));

    }

    public Queue<ScoreUpdate> getUpdates() {

        final Queue<ScoreUpdate> queue = new ConcurrentLinkedQueue<ScoreUpdate>();
        queue.addAll(updates.values());
        updates.clear();
        return queue;

    }

    public Queue<Query> getInfoQueries() {

        return queries;

    }

    public void addRankQuery(Player player) {

        queries.add(new RankQuery(this, player));

    }

    public void addTopQuery(Player player, GUIHandler guiHandler, int page, int numberOfPages) {

        queries.add(new TopQuery(this, guiHandler, player, page, numberOfPages));

    }

    public void addResult(Result result) {

        results.add(result);

    }

    public Queue<Result> getResults() {

        return results;

    }

    public void setNumberOfPages(int pagesTotal) {

        numberOfPages = pagesTotal;

    }

    public int getNumberOfPages() {

        return numberOfPages;

    }

    public Quarantine getPlugin() {

        return plugin;

    }

    public void disconnect() {

        timer.cancel();
        plugin.getServer().getScheduler().cancelTask(displayInfoTaskID);
        lbdb.disconnect();

    }
}
