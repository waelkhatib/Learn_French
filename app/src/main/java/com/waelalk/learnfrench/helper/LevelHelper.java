package com.waelalk.learnfrench.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.widget.ImageView;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.Initialization;
import com.waelalk.learnfrench.model.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LevelHelper {
    public static final int MY_PERMISSIONS_WRITE =43 ;
    private static final int QuestionCount = 3;
    private static List<Integer> QuestionOfLevel2;
    private static final int TotalPoint = 100;


    public static List<Integer> getQuestionOfLevel1() {
        return QuestionOfLevel1;
    }

    public static void setQuestionOfLevel1(List<Integer> questionOfLevel1) {
        QuestionOfLevel1 = questionOfLevel1;
    }

    public static List<Integer> getQuestionOfLevel2() {
        return QuestionOfLevel2;
    }

    public static void setQuestionOfLevel2(List<Integer> questionOfLevel2) {
        QuestionOfLevel2 = questionOfLevel2;
    }

    public static List<Integer> getQuestionOfLevel3() {
        return QuestionOfLevel3;
    }

    public static void setQuestionOfLevel3(List<Integer> questionOfLevel3) {
        QuestionOfLevel3 = questionOfLevel3;
    }

    private static List<Integer> QuestionOfLevel3;

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        LevelHelper.game = game;
    }

    private static Game game;
    // --Commented out by Inspection (13/09/2020 23:22):private boolean is_released;

    public static String getSharedPrefs() {
        return SHARED_PREFS;
    }

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "myKey";

    public static String getKEY() {
        return KEY;
    }

    public  SharedPreferences getSharedPreferences(){
        return context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    public static int getQuestionCount() {
        return QuestionCount;
    }

    private static final int BANK_QUESTION_NO = 167;
    private static final List<Integer> list = new ArrayList<>();
    // --Commented out by Inspection (13/09/2020 23:29):private static final int request_code=32;
    private static List<Integer> QuestionOfLevel1;
    public static int getPointValue(){
        return TotalPoint/QuestionCount;
    }

    static {
        for (int i = 1; i <= BANK_QUESTION_NO; i++) {
            list.add(i);
        }
    }

// --Commented out by Inspection START (13/09/2020 23:22):
//    public static int getRequestCode() {
//
//        return request_code;
//    }
// --Commented out by Inspection STOP (13/09/2020 23:22)

    public Context getContext() {
        return context;
    }

    // --Commented out by Inspection (13/09/2020 23:29):private Context context;

    public static DBHelper getDbHelper() {
        return dbHelper;
    }

    private static DBHelper dbHelper;

    public static void setDbHelper(DBHelper dbHelper) {
        LevelHelper.dbHelper = dbHelper;
    }

    public MediaPlayer getMainPlayer() {
        return mainPlayer;
    }

    private Context context;
    // --Commented out by Inspection START (13/09/2020 23:22):
    private MediaPlayer mainPlayer;
    private  MediaPlayer actionPlayer;
    public  void runBackgroundMusic(){
        mainPlayer=MediaPlayer.create(context,R.raw.music);
        mainPlayer.setLooping(true);
        mainPlayer.start();
    }


    //
    public LevelHelper(Context context) {
        this.context = context;

    }

    public static List<Integer> generateRandomQuesions(int count,int level){
        List<Integer> listOfID = new ArrayList<>();
        List<Integer> temp=list.subList((BANK_QUESTION_NO/3)*(level-1),(BANK_QUESTION_NO/3)*(level)-1);
            Collections.shuffle(temp);
            for (int i=0; i<count; i++) {
                listOfID.add(temp.get(i));
            }

        return listOfID;
    }
    public static List<Integer> generateRandomOptions(int count,int qId){
        List<Integer> listOfID = new ArrayList<>();
           listOfID.add(qId);
        list.remove(qId);
            Collections.shuffle(list);
            for (int i=0; i<count-1; i++) {
                listOfID.add(list.get(i));
            }
            list.add(qId);
        return listOfID;
    }



    public void startWinTone() {
        actionPlayer=MediaPlayer.create(context,R.raw.correct);
        actionPlayer.start();

        actionPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    public void startFailTone() {
        actionPlayer = MediaPlayer.create(context, R.raw.wrong);
        actionPlayer.start();

        actionPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public void startGameOverTone() {
        actionPlayer = MediaPlayer.create(context, R.raw.game_over);
        actionPlayer.start();

        actionPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public void startVictoryTone() {
        actionPlayer = MediaPlayer.create(context, R.raw.victory);
        actionPlayer.start();

        actionPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }


    public Resources getResources() {
        return context.getResources();
    }

    public String getPackageName() {
        return context.getPackageName();
    }

    public void makeMessageBox( final int action, final Initialization behaviorActivity){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (action){
                    case 1:getMainPlayer().start();behaviorActivity. initViews(); break;
                    case 2:behaviorActivity. finish(); break;
                    default:break;
                }
            }
        },4000);
        /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!is_released&&getActionPlayer().isPlaying()) {
                            getActionPlayer().stop();
                            getActionPlayer().release();
                            is_released=true;
                        }
                        dialog.dismiss();
                        switch (action){
                            case 1:getMainPlayer().start();behaviorActivity. initViews(); break;
                            case 2:behaviorActivity. finish(); break;
                            default:break;
                        }


                    }
                });
        builder.setCancelable(false);
        builder.show();*/
    }
    public static void makeMessageBox( String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.info);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                     }
                });
        builder.setCancelable(false);
        builder.show();
    }

    public String getString(int resId) {
        return context.getString(resId);
    }
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static boolean checkImageResource(Context ctx, ImageView imageView,
                                             int imageResource) {
        boolean result = false;

        if (ctx != null && imageView != null && imageView.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }

            if (imageView.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }

        return result;
    }


}
