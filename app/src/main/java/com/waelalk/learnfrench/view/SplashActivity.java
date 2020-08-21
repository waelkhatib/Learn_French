package com.waelalk.learnfrench.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Game;
import com.waelalk.learnfrench.model.Level;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences sharedPreferences = getSharedPreferences(LevelHelper.getSharedPrefs(), MODE_PRIVATE);
                String text = sharedPreferences.getString(LevelHelper.getKEY(), "");
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
}
