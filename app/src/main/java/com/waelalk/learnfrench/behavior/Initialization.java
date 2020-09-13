package com.waelalk.learnfrench.behavior;

public interface Initialization {
    public void initViews();
    public void finish();
    public void startMusic();
    public void stoptMusic();
    public void share();

    public void resumeMusic();
    public void setStatusBarTransparent();
    public void checkAnswer(CharSequence answer);
    public void  makeEffect();
    public String SaveBackground();
}
