package com.waelalk.learnfrench.model;

import java.util.Objects;

public class Translation {
    private int id;
    private String synonym;
    private String synonym_ar;
    private boolean is_correct;

    public Translation(int id, String synonym, String synonym_ar, boolean is_correct) {
        this.id = id;
        this.synonym = synonym;
        this.synonym_ar = synonym_ar;
        this.is_correct = is_correct;
    }
    public static  Translation getInstanceWithSynonym(String synonym){
        return new Translation(-1,synonym,"",false);
    }
    public static  Translation getInstanceWithSynonymAr(String synonym_ar){
        return new Translation(-1,"",synonym_ar,false);
    }

    public int getId() {
        return id;
    }

    public boolean isCorrect() {
        return is_correct;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getSynonym_ar() {
        return synonym_ar;
    }

    public void setSynonym_ar(String synonym_ar) {
        this.synonym_ar = synonym_ar;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translation that = (Translation) o;
        return Objects.equals(synonym, that.synonym) ||
                Objects.equals(synonym_ar, that.synonym_ar);
    }

    @Override
    public int hashCode() {

        return Objects.hash(synonym, synonym_ar);
    }
}
