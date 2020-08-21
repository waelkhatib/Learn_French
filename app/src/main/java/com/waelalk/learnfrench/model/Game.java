package com.waelalk.learnfrench.model;

public class Game {
    private int generalLevelNo=1;
    private Level level=null;


    public int getGeneralevelNo() {
        return generalLevelNo;
    }

    public void updateGeneralLevelNo() {
        this.generalLevelNo = Math.max(generalLevelNo,getLevel().getLevelNo());
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
