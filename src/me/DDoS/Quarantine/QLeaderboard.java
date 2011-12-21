package me.DDoS.Quarantine;

import com.agoragames.leaderboard.LeaderData;
import com.agoragames.leaderboard.Leaderboard;
import java.util.ArrayList;
import java.util.List;
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

    public QLeaderboard(String zoneName) {

        lb = new Leaderboard(zoneName, HOST, PORT, 5);

    }

    public void registerPlayer(String playerName, int score) {

        try {

            lb.rankMember(playerName, score);

        } catch (JedisConnectionException e) {
            
            Quarantine.log.info("[Quarantine] Couldn't connect to Redis server. Is it on?");
            
        }
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

        lb.disconnect();

    }
}