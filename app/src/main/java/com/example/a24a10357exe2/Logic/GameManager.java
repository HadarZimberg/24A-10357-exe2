package com.example.a24a10357exe2.Logic;

public class GameManager {

    public final int RIGHT_ANSWER_POINTS = 10;
    public final int METERS_PER_SECOND = 5;
    private int life;
    private int crashes = 0;
    private int score = 0;
    private int odometer = 0;

    public GameManager(int life) {
        this.life = life;
    }

    public int getLife() {
        return this.life;
    }

    public int getCrashes() {
        return this.crashes;
    }

    public int getScore() {
        return this.score;
    }
    public int getOdometer() {
        return this.odometer;
    }

    public GameManager setCrashes(int crashes) {
        this.crashes = crashes;
        return this;
    }

    public void updateScore() {
        score += RIGHT_ANSWER_POINTS;
    }

    public void updateOdometer() {
        odometer += METERS_PER_SECOND;
    }

    public boolean isGameEnded() {
        return getLife() == getCrashes();
    }
}
