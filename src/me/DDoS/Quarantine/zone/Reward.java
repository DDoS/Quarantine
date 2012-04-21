package me.DDoS.Quarantine.zone;

import java.util.Random;

/**
 *
 * @author DDoS
 */
public class Reward {
    
    private static final Random RANDOM = new Random();
    //
    private final int min;
    private final int max;
    private final int score;
    
    public Reward(int min, int max, int score) {
        
        this.min = min;
        this.max = max;
        this.score = score;
        
    }
    
    public int getRandomMoneyAmount() {

        return RANDOM.nextInt(max - min + 1) + min;
        
    }
    
    public int getScoreReward() {
        
        return score;
        
    }
}
