package me.DDoS.Quarantine.leaderboard;

/**
 *
 * @author DDoS
 */
public class ScoreUpdate {
    
    private final String player;
    private final int score;

    public ScoreUpdate(String player, int score) {
        
        this.player = player;       
        this.score = score;
    
    }
    
    public String getPlayer() {
        
        return player;
        
    }
    
    public int getScore() {
        
        return score;
        
    }
}
