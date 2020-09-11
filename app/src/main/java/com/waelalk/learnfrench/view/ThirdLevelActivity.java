package com.waelalk.learnfrench.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.Initialization;
import com.waelalk.learnfrench.behavior.ThirdLevelBehavior;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;

import static com.waelalk.learnfrench.helper.LevelHelper.MY_PERMISSIONS_WRITE;

public class ThirdLevelActivity extends AppCompatActivity implements View.OnClickListener {

    private Initialization behavior;
    private EditText input_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_level);
        LevelHelper levelHelper=new LevelHelper(this);
        Intent intent=getIntent();
        String content=intent.getStringExtra(LevelHelper.getKEY());
        Level level=content!=null?LevelHelper.getGame().getLevel() : new Level(3);
        behavior=new ThirdLevelBehavior(this,levelHelper,level);
        behavior.setStatusBarTransparent();
        input_text=(EditText)findViewById(R.id.input_txt);
        input_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    //Find the currently focused view, so we can grab the correct window token from it.
                    View view = getCurrentFocus();
                    //If no view currently has focus, create a new one, just so we can grab a window token from it
                    if (view == null) {
                        view = new View(getApplicationContext());
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    input_text.clearFocus();
                    findViewById(R.id.ok).performClick();
                }
                return false;
            }
        });
        behavior.startMusic();
        behavior. initViews();
    }


    @Override
    public void onClick(View v) {

        if(v==findViewById(R.id.play)){
            behavior.stoptMusic();
            MediaPlayer mPlayer = MediaPlayer.create(this, getResources().getIdentifier("w"+((ThirdLevelBehavior)behavior).getMediaID(),"raw",getPackageName()));
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    behavior.resumeMusic();
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
    @Override
    protected void onResume() {
        super.onResume();
        behavior.resumeMusic();
        behavior.initGraphic();
    }
    @Override
    public void onBackPressed() {
        behavior.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_WRITE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            behavior.share();
        }
    }
}
