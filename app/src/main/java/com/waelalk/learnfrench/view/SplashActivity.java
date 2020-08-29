package com.waelalk.learnfrench.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.DBHelper;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Game;
import com.waelalk.learnfrench.model.Level;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                LevelHelper.setDbHelper(new DBHelper(getApplicationContext()));
                LevelHelper.setQuestionOfLevel1(LevelHelper.generateRandomQuesions(LevelHelper.getQuestionCount()+2,1));
                LevelHelper.setQuestionOfLevel2(LevelHelper.generateRandomQuesions(LevelHelper.getQuestionCount()+2,2));
                LevelHelper.setQuestionOfLevel3(LevelHelper.generateRandomQuesions(LevelHelper.getQuestionCount()+2,3));

            }
        }).start();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences sharedPreferences = getSharedPreferences(LevelHelper.getSharedPrefs(), MODE_PRIVATE);
                String text = sharedPreferences.getString(LevelHelper.getKEY(), "");
                Log.d("kad",text);
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebView webV=new WebView(getApplicationContext() );
                webV.setVisibility(View.GONE);
                webV.loadUrl("about:blank");
                WebSettings settings =webV. getSettings();
                settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
                settings.setJavaScriptEnabled(true);
                settings.setLoadWithOverviewMode(true);
                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                settings.setDomStorageEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    // chromium, enable hardware acceleration
                    webV.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                } else {
                    // older android version, disable hardware acceleration
                    webV.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
                webV.setId(R.id.emot);

                LevelHelper.setSharedWebView(webV);


            }
        });
    }
}
