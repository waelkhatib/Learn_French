package com.waelalk.learnfrench.model;

import java.util.Objects;

public class Translation {
    private final int id;
    private final String synonym;
    private String synonym_ar;
    private final boolean is_correct;

    public Translation(int id, String synonym, String synonym_ar, boolean is_correct) {
        this.id = id;
        this.synonym = synonym;
        this.synonym_ar = synonym_ar;
        this.is_correct = is_correct;
    }

    // --Commented out by Inspection START (13/09/2020 23:22):
//    public static  Translation getInstanceWithSynonym(String synonym){
// --Commented out by Inspection START (13/09/2020 23:22):
////        return new Translation(-1,synonym,"",false);
////    }
//// --Commented out by Inspection STOP (13/09/2020 23:22)
// --Commented out by Inspection START (13/09/2020 23:22):
////    public static  Translation getInstanceWithSynonymAr(String synonym_ar){
//// --Commented out by Inspection STOP (13/09/2020 23:22)
// --Commented out by Inspection START (13/09/2020 23:22):
//// --Commented out by Inspection STOP (13/09/2020 23:22)
// --Commented out by Inspection START (13/09/2020 23:22):
////        return new Translation(-1,"",synonym_ar,false);
//// --Commented out by Inspection STOP (13/09/2020 23:22)
//    }
//
    public int getId() {

        return id;
    }

    public boolean isCorrect() {
        return is_correct;
    }

    // --Commented out by Inspection START (13/09/2020 23:22):
//    public void setId(int id) {
//        this.id = id;
//    }
//
    public String getSynonym() {
        return synonym;
// --Commented out by Inspection STOP (13/09/2020 23:22)
    }

    // --Commented out by Inspection START (22/09/2020 08:12):
//    public void setSynonym(String synonym) {
//        this.synonym = synonym;
//    }
// --Commented out by Inspection START (22/09/2020 08:12):
//// --Commented out by Inspection STOP (22/09/2020 08:12)
//
    public String getSynonym_ar() {
        return synonym_ar;
// --Commented out by Inspection STOP (22/09/2020 08:12)
    }

    public void setSynonym_ar(String synonym_ar) {
        this.synonym_ar = synonym_ar;
    }

// --Commented out by Inspection START (22/09/2020 08:12):
//    public void setIs_correct(boolean is_correct) {
//        this.is_correct = is_correct;
//    }
// --Commented out by Inspection STOP (22/09/2020 08:12)

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
