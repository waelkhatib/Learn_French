package com.waelalk.learnfrench.view;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.Initialization;
import com.waelalk.learnfrench.behavior.ThirdLevelBehavior;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;

public class ThirdLevelActivity extends AppCompatActivity implements View.OnClickListener {

    private Initialization behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_level);
        LevelHelper levelHelper=new LevelHelper(this);
        behavior=new ThirdLevelBehavior(this,levelHelper, new Level(3));
        behavior.startMusic();
        behavior. initViews();
    }


    @Override
    public void onClick(View v) {
        EditText input_text=(EditText)findViewById(R.id.input_txt);
        if(v==findViewById(R.id.play)){
            MediaPlayer mPlayer = MediaPlayer.create(this, getResources().getIdentifier("w"+((ThirdLevelBehavior)behavior).getMediaID(),"raw",getPackageName()));
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }else
        if(v==findViewById(R.id.ok)){
            if(input_text.getText().toString().length()==0){
                LevelHelper.makeMessageBox(getString(R.string.Please_fill_the_answer_field),this);
            }else {
                behavior.checkAnswer(input_text.getText().toString());
            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        behavior.stoptMusic();
    }


}
