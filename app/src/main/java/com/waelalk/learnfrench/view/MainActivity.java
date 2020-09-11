package com.waelalk.learnfrench.view;

import android.content.Intent;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.LevelHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long start=System.currentTimeMillis();
        setContentView(R.layout.activity_main);
       // ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //Glide.with(this).asGif().load(R.drawable.win).into(imageView);
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
            startActivityForResult(intent,LevelHelper.getRequestCode());
        }else
        if(v==findViewById(R.id.card2) ){
            if(LevelHelper.checkImageResource(this,((ImageView)findViewById(R.id.lock2)),R.drawable.open_lock)) {
                Intent intent = new Intent(this, SecondLevelActivity.class);
                startActivityForResult(intent,LevelHelper.getRequestCode());
            }
            else
            LevelHelper.makeMessageBox(getString(R.string.This_level_is_locked),this);
        }else
        if(v==findViewById(R.id.card3) ){
            if(LevelHelper.checkImageResource(this,((ImageView)findViewById(R.id.lock3)),R.drawable.open_lock)){
            Intent intent=new Intent(this,ThirdLevelActivity.class);
                startActivityForResult(intent,LevelHelper.getRequestCode());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          checkIntent(data);


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
