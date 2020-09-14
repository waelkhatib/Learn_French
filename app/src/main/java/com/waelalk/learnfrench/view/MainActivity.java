package com.waelalk.learnfrench.view;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.LevelHelper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;
    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        checkIntent(intent);
                        // Handle the Intent
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long start=System.currentTimeMillis();
        setContentView(R.layout.activity_main);

        Log.d("time",""+(System.currentTimeMillis()-start)/1000.0);
        checkIntent(getIntent());
        mediaPlayer=MediaPlayer.create(this,R.raw.music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

    }

    @Override
    public void onClick(View v) {
        if(v==findViewById(R.id.card1)){
            Intent intent=new Intent(this,FirstLevelActivity.class);
            mStartForResult.launch(intent);
        }else
        if(v==findViewById(R.id.card2) ){
            if(LevelHelper.checkImageResource(this,((ImageView)findViewById(R.id.lock2)),R.drawable.open_lock)) {
                Intent intent = new Intent(this, SecondLevelActivity.class);
                mStartForResult.launch(intent);
            }
            else
            LevelHelper.makeMessageBox(getString(R.string.This_level_is_locked),this);
        }else
        if(v==findViewById(R.id.card3) ){
            if(LevelHelper.checkImageResource(this,((ImageView)findViewById(R.id.lock3)),R.drawable.open_lock)){
            Intent intent=new Intent(this,ThirdLevelActivity.class);
                mStartForResult.launch(intent);
            }
            else
                LevelHelper.makeMessageBox(getString(R.string.This_level_is_locked),this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }



    private void checkIntent(Intent data) {
        int levelNo=data.getIntExtra("levelNo",-1);
        if(levelNo==2){
            ((ImageView)findViewById(R.id.lock2)).setImageResource(R.drawable.open_lock);
        }else
        if(levelNo>2){
            ((ImageView)findViewById(R.id.lock2)).setImageResource(R.drawable.open_lock);
            ((ImageView)findViewById(R.id.lock3)).setImageResource(R.drawable.open_lock);
        }
    }
}
