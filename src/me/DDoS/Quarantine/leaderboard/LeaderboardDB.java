package me.DDoS.Quarantine.leaderboard;

import java.util.List;

/**
 *
 * @author DDoS
 */
public interface LeaderboardDB {
    
    public void disconnect();
    
    public boolean hasConnection();
    
    public void rank(String member, int score);
    
    public void sort();
    
    public int getPlayerTotal();
    
    public int getPageTotal();
    
    public int getScore(String member);
    
    public boolean isMember(String member);
    
    public int getRank(String member);
    
    public List<LeaderData> getLeaders(int startingPage, int numberOfPages);
        
}
