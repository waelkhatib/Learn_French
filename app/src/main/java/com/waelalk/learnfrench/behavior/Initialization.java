package com.waelalk.learnfrench.behavior;

import com.waelalk.learnfrench.model.Level;

public interface Initialization {
    public void initViews();
    public void finish();
    public void startMusic();
    public void stoptMusic();
    public void share();
    public void initGraphic();
    public void resumeMusic();
    public void checkAnswer(CharSequence answer);
    public void  makeEffect();
}
