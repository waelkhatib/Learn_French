package com.waelalk.learnfrench.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.Initialization;
import com.waelalk.learnfrench.behavior.SecondLevelBehavior;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;

public class SecondLevelActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] viewIDs=new int[]{R.id.option1,R.id.option2,R.id.option3,R.id.option4};
    private Initialization behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_level);
        LevelHelper levelHelper=new LevelHelper(this);
        behavior=new SecondLevelBehavior(this,levelHelper, new Level(2));
        behavior.startMusic();

        behavior. initViews();
    }


    @Override
    public void onClick(View v) {
        boolean find=false;
        for (int i=0;i<viewIDs.length;i++){
            if(v==findViewById(viewIDs[i])){
                find=true;
                behavior.checkAnswer(((Button)v).getText());
                break;
            }
        }
        if(!find){

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        behavior.stoptMusic();
    }
}
