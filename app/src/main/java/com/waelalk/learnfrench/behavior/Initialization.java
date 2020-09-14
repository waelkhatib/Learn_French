package com.waelalk.learnfrench.behavior;

public interface Initialization {
    void initViews();

    void finish();

    void startMusic();

    void stoptMusic();

    void share();

    void resumeMusic();

    void setStatusBarTransparent();

    void checkAnswer(CharSequence answer);

    void makeEffect();
}
