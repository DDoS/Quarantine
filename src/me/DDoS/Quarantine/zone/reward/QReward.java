package me.DDoS.Quarantine.zone.reward;

import java.util.Random;

/**
 *
 * @author DDoS
 */
public class QReward {
    
    private final int min;
    private final int max;
    private final int score;
    //
    private final Random random = new Random();
    
    public QReward(int min, int max, int score) {
        
        this.min = min;
        this.max = max;
        this.score = score;
        
    }
    
    public int getRandomMoneyAmount() {

        return random.nextInt(max - min + 1) + min;
        
    }
    
    public int getScoreReward() {
        
        return score;
        
    }
}
