package me.DDoS.Quarantine.leaderboard;

import java.util.List;

/**
 *
 * @author DDoS
 */
public interface LeaderboardDB {
    
    public void disconnect();
    
    public int rank(String member, double score);
    
    public int getScore(String member);
    
    public boolean isMember(String member);
    
    public int getRank(String member);
    
    public List<LeaderData> getLeaders(int currentPage);
    
}
