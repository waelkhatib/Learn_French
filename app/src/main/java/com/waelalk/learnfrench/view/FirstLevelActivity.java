package com.waelalk.learnfrench.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.FirstLevelBehavior;
import com.waelalk.learnfrench.behavior.Initialization;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;

import java.time.Duration;
import java.time.Instant;

public class FirstLevelActivity extends AppCompatActivity  implements View.OnClickListener {


   private int[] viewIDs=new int[]{R.id.option1,R.id.option2,R.id.option3,R.id.option4};
   private Initialization behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long start=System.currentTimeMillis();
        setContentView(R.layout.activity_first_level);
        Log.d("time",""+(System.currentTimeMillis()-start)/1000.0);
       LevelHelper levelHelper=new LevelHelper(this);
        Intent intent=getIntent();
        String content=intent.getStringExtra(LevelHelper.getKEY());
        Level level=content!=null?LevelHelper.getGame().getLevel() : new Level(1);
        behavior=new FirstLevelBehavior(this,levelHelper, level);
       behavior.startMusic();
       behavior. initViews();





    }


    @Override
    public void onClick(View v) {
        for (int i = 0; i < viewIDs.length; i++) {
            if (v == findViewById(viewIDs[i])) {
                behavior.makeEffect();
                behavior.checkAnswer(((Button) v).getText());
                break;
            }

    }

    }

    @Override
    public void onBackPressed() {
        behavior.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        behavior.resumeMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        behavior.stoptMusic();
    }
}
