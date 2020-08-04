package com.waelalk.learnfrench.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.FirstLevelBehavior;
import com.waelalk.learnfrench.behavior.Initialization;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;

public class FirstLevelActivity extends AppCompatActivity  implements View.OnClickListener {


   private int[] viewIDs=new int[]{R.id.option1,R.id.option2,R.id.option3,R.id.option4};
   private Initialization behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_level);
        LevelHelper levelHelper=new LevelHelper(this);
        behavior=new FirstLevelBehavior(this,levelHelper, new Level(1));
        behavior.startMusic();
       behavior. initViews();
        findViewById(R.id.share).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
    if(v==findViewById(R.id.share)){
     behavior.share();
    }else {
        for (int i = 0; i < viewIDs.length; i++) {
            if (v == findViewById(viewIDs[i])) {

                behavior.checkAnswer(((Button) v).getText());
                break;
            }
        }
    }

    }



    @Override
    protected void onPause() {
        super.onPause();
        behavior.stoptMusic();
    }
}
