package com.waelalk.learnfrench.model;

import com.waelalk.learnfrench.helper.LevelHelper;

import java.util.List;

public class Level {
    private int levelNo;
    private int questionNo;

    public List<Integer> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Integer> questions) {
        this.questions = questions;
    }

    private List<Integer> questions;
    private int chancesNo;
    private int points;

    public Level(int levelNo) {
        this.levelNo = levelNo;
        this.chancesNo = 3;
        this.points = 0;
        this.questionNo = 1;
        this.questions=LevelHelper.generateRandomQuesions(LevelHelper.getQuestionCount()+2);
    }

    public Level(int levelNo, int questionNo, List<Integer> current_question, int chancesNo, int points) {
        this.levelNo = levelNo;
        this.questionNo = questionNo;
        this.questions = current_question;
        this.chancesNo = chancesNo;
        this.points = points;
    }

    public int getQuestionNo() {
        return questionNo;
    }
    public int getQuestionNoForHeader() {
        return questionNo>LevelHelper.getQuestionCount()?LevelHelper.getQuestionCount():questionNo;
    }

    public void updateQuestionNo(int delta) {
        this.questionNo +=delta;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }



    public int getChancesNo() {
        return chancesNo;
    }

    public void updateChancesNo(int delta) {
        this.chancesNo +=delta;
    }

    public int getPoints() {
        return points;
    }

    public void updatePoints(int delta) {
        if(delta>=0){
            this.points +=delta;
        }else
        if(delta+this.points>=0)
            this.points +=delta;
    }
}
