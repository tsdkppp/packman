package model;

public class GameStats {

    private int score  = 0;
    private int lives  = 3;
    private int second = 45;

    private boolean hasKey = false;
    private volatile boolean timerFrozen  = false;
    private volatile boolean enemyFrozen  = false;

    public  boolean isTimerFrozen()  { return timerFrozen;  }
    public  boolean isEnemyFrozen()  { return enemyFrozen;  }

    public synchronized void freezeTimer (boolean v){ timerFrozen = v; }
    public synchronized void freezeEnemy (boolean v){ enemyFrozen = v; }


    public synchronized boolean hasKey() { return hasKey; }
    public synchronized void setKey(boolean value) { hasKey = value; }


    public synchronized int getScore()  { return score;  }
    public synchronized int getLives()  { return lives;  }
    public synchronized int getSecond() { return second; }

    public synchronized void addScore(int pts) { score += pts; }
    public synchronized void addLife(int d)    { lives += d; }   // d can be -1
    public synchronized void decSecond()       { second--; }
}
