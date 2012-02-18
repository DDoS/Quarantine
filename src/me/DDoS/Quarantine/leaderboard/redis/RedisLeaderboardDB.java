package me.DDoS.Quarantine.leaderboard.redis;

import me.DDoS.Quarantine.leaderboard.LeaderData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import me.DDoS.Quarantine.leaderboard.LeaderboardDB;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class RedisLeaderboardDB implements LeaderboardDB {

    private final Jedis jedis;
    private final String lbName;
    private final int pageSize;

    public RedisLeaderboardDB(String lbName, String host, int port, int pageSize) {

        this.lbName = lbName;
        this.pageSize = pageSize;
        this.jedis = new Jedis(host, port);

    }

    @Override
    public void disconnect() {

        jedis.disconnect();

    }

    private long totalMembers() {

        return jedis.zcard(lbName);

    }

    private int totalPages() {

        return (int) Math.ceil((float) totalMembers() / (float) pageSize);

    }

    @Override
    public int rank(String member, double score) {

        return jedis.zadd(lbName, score, member).intValue();

    }

    @Override
    public int getScore(String member) {

        return jedis.zscore(lbName, member).intValue();

    }

    @Override
    public boolean isMember(String member) {

        return jedis.zscore(lbName, member) != null;

    }

    @Override
    public int getRank(String member) {

        return jedis.zrevrank(lbName, member).intValue() + 1;

    }

    @Override
    public List<LeaderData> getLeaders(int currentPage) {

        if (currentPage < 1) {

            currentPage = 1;

        }

        if (currentPage > totalPages()) {

            currentPage = totalPages();

        }

        int indexForRedis = currentPage - 1;
        int startingOffset = indexForRedis * pageSize;

        if (startingOffset < 0) {

            startingOffset = 0;

        }

        int endingOffset = (startingOffset + pageSize) - 1;

        Set<Tuple> rawLeaderData = jedis.zrevrangeWithScores(lbName, startingOffset, endingOffset);
        return generateLeaderData(rawLeaderData);

    }

    private List<LeaderData> generateLeaderData(Set<Tuple> memberData) {

        List<LeaderData> leaderData = new ArrayList<LeaderData>();

        Iterator<Tuple> memberDataIterator = memberData.iterator();

        while (memberDataIterator.hasNext()) {

            Tuple memberDataTuple = memberDataIterator.next();
            LeaderData leaderDataItem = new LeaderData(memberDataTuple.getElement(), (int) memberDataTuple.getScore(),
                    getRank(memberDataTuple.getElement()));
            leaderData.add(leaderDataItem);

        }

        return leaderData;

    }
}
