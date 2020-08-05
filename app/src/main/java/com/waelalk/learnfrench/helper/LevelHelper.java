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
import android.widget.ImageView;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.Initialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LevelHelper {
    private static final int request_code=32;

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

    private static int QuestionCount=2;
    private static int TotalPoint=100;
    public static int getPointValue(){
        return TotalPoint/QuestionCount;
    }
    private static List<Integer> list = new ArrayList<Integer>();

    public static int getRequestCode() {

        return request_code;
    }

    public Context getContext() {
        return context;
    }

    private Context context;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    private DBHelper dbHelper;

    public MediaPlayer getMainPlayer() {
        return mainPlayer;
    }

    private  MediaPlayer mainPlayer;

    public LevelHelper(Context context) {
        this.context = context;
        dbHelper=new DBHelper(context);
    }

    public MediaPlayer getActionPlayer() {
        return actionPlayer;
    }

    public void setActionPlayer(MediaPlayer actionPlayer) {
        this.actionPlayer = actionPlayer;
    }

    private  MediaPlayer actionPlayer;
    public  void runBackgroundMusic(){
        mainPlayer=MediaPlayer.create(context,R.raw.music);
        mainPlayer.setLooping(true);
        mainPlayer.start();
    }

    static {
        for (int i = 1; i < 16; i++) {
            list.add(new Integer(i));
        }
    }
    public static List<Integer> generateRandomQuesions(int count){
        List<Integer> listOfID = new ArrayList<Integer>();
            Collections.shuffle(list);
            for (int i=0; i<count; i++) {
                listOfID.add(list.get(i));
            }

        return listOfID;
    }
    public static List<Integer> generateRandomOptions(int count,int qId){
        List<Integer> listOfID = new ArrayList<Integer>();
           listOfID.add(qId);
           list.remove(list.indexOf(qId));
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

    public void makeMessageBox(String title, String message, final int action, final Initialization behaviorActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(getActionPlayer().isPlaying()) {
                            getActionPlayer().stop();
                            getActionPlayer().release();
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
        builder.show();
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
