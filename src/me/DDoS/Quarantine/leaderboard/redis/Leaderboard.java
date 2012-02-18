package me.DDoS.Quarantine.leaderboard.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class Leaderboard {

    private final Jedis jedis;
    private final String lbName;
    private final int pageSize;

    public Leaderboard(String lbName, String host, int port, int pageSize) {
        
        this.lbName = lbName;
        this.pageSize = pageSize;
        this.jedis = new Jedis(host, port);

    }
    
    public void disconnect() {
        
        jedis.disconnect();
    
    }

    public long totalMembers() {
         
        return jedis.zcard(lbName);
    
    }


    public int totalPages(Integer pageSize) {
        
        if (pageSize == null) {
           
            pageSize = this.pageSize;
        
        }

        return (int) Math.ceil((float) totalMembers() / (float) pageSize);
    
    }

    public long rankMember(String member, double score) {
        
        return jedis.zadd(lbName, score, member);
    
    }

    public double scoreFor(String member) {
        
        return jedis.zscore(lbName, member);
    
    }

    public boolean checkMember(String member) {
       
        return !(jedis.zscore(lbName, member) == null);
    
    }

    public long rankFor(String member, boolean useZeroIndexForRank) {
        
        if (useZeroIndexForRank) {
         
            return jedis.zrevrank(lbName, member);
        
        } else {
         
            return (jedis.zrevrank(lbName, member) + 1);
        
        }  
    }

    public List<LeaderData> leadersIn(int currentPage, boolean useZeroIndexForRank) {
        
        if (currentPage < 1) {
        
            currentPage = 1;
        
        }

        if (currentPage > totalPages(pageSize)) {
         
            currentPage = totalPages(pageSize);
        
        }

        int indexForRedis = currentPage - 1;
        int startingOffset = indexForRedis * pageSize;
        
        if (startingOffset < 0) {
          
            startingOffset = 0;
        
        }
        
        int endingOffset = (startingOffset + pageSize) - 1;

        Set<Tuple> rawLeaderData = jedis.zrevrangeWithScores(lbName, startingOffset, endingOffset);
        return massageLeaderData(rawLeaderData, useZeroIndexForRank);
    
    }

    private List<LeaderData> massageLeaderData(Set<Tuple> memberData, boolean useZeroIndexForRank) {
        
        List<LeaderData> leaderData = new ArrayList<LeaderData>();

        Iterator<Tuple> memberDataIterator = memberData.iterator();
        
        while (memberDataIterator.hasNext()) {
            
            Tuple memberDataTuple = memberDataIterator.next();
            LeaderData leaderDataItem = new LeaderData(memberDataTuple.getElement(), memberDataTuple.getScore(),
                    rankFor(memberDataTuple.getElement(), useZeroIndexForRank));
            leaderData.add(leaderDataItem);
        
        }

        return leaderData;
    
    }
}
