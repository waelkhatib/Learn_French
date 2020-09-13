package com.waelalk.learnfrench.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.DBHelper;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Game;
import com.waelalk.learnfrench.model.Level;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                LevelHelper.setDbHelper(new DBHelper(getApplicationContext()));
                LevelHelper.setQuestionOfLevel1(LevelHelper.generateRandomQuesions(LevelHelper.getQuestionCount()+2,1));
                LevelHelper.setQuestionOfLevel2(LevelHelper.generateRandomQuesions(LevelHelper.getQuestionCount()+2,2));
                LevelHelper.setQuestionOfLevel3(LevelHelper.generateRandomQuesions(LevelHelper.getQuestionCount()+2,3));


            }
        }).start();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences sharedPreferences = getSharedPreferences(LevelHelper.getSharedPrefs(), MODE_PRIVATE);
                String text = sharedPreferences.getString(LevelHelper.getKEY(), "");
                Log.d("kad","--"+getResources().getDisplayMetrics().density);
                if(text.equals("")) {
                    LevelHelper.setGame(new Game());
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    LevelHelper.setGame(new Gson().fromJson(text,Game.class));
                    Level level=LevelHelper.getGame().getLevel();
                    Intent i;
                    switch (level.getLevelNo()){
                        case 1:
                            i = new Intent(SplashActivity.this, FirstLevelActivity.class);
                            i.putExtra(LevelHelper.getKEY(),text);
                            startActivityForResult(i,LevelHelper.getRequestCode());
                            break;
                        case 2:
                            i = new Intent(SplashActivity.this, SecondLevelActivity.class);
                            i.putExtra(LevelHelper.getKEY(),text);
                            startActivityForResult(i,LevelHelper.getRequestCode());
                            break;
                        case 3:
                            i = new Intent(SplashActivity.this, ThirdLevelActivity.class);
                            i.putExtra(LevelHelper.getKEY(),text);
                            startActivityForResult(i,LevelHelper.getRequestCode());
                            break;
                        default:
                          i = new Intent(SplashActivity.this, MainActivity.class);
                            i.putExtra("levelNo",level.getLevelNo());
                            startActivity(i);
                            break;
                    }
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    private ViewGroup inflateLayout(int layoutId){
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(layoutId, null);
        return (ViewGroup)v.findViewById(R.id.rlt_layout);

    }

    @Override
    protected void onResume() {
        super.onResume();



    }
}
