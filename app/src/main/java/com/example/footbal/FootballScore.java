package com.example.footbal;

public class FootballScore {
    private int firstScore, secondScore;
    private String firstCommand;
    private String secondCommand;

    public FootballScore(int firstScore, int secondScore, String firstCommand, String secondCommand) {
        this.firstScore = firstScore;
        this.secondScore = secondScore;
        this.firstCommand = firstCommand;
        this.secondCommand = secondCommand;
    }

    public String getFirstCommand() {
        return firstCommand;
    }

    public int getFirstScore() {
        return firstScore;
    }

    public int getSecondScore() {
        return secondScore;
    }

    public String getSecondCommand() {
        return secondCommand;
    }

    public void setFirstScore(int firstScore) {
        this.firstScore = firstScore;
    }

    public void setSecondScore(int secondScore) {
        this.secondScore = secondScore;
    }
}
