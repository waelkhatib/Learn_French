package com.waelalk.learnfrench.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.Initialization;
import com.waelalk.learnfrench.behavior.SecondLevelBehavior;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;

import static com.waelalk.learnfrench.helper.LevelHelper.MY_PERMISSIONS_WRITE;

public class SecondLevelActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] viewIDs = new int[]{R.id.option1, R.id.option2, R.id.option3, R.id.option4};
    private Initialization behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_level);
        LevelHelper levelHelper = new LevelHelper(this);
        Intent intent = getIntent();
        String content = intent.getStringExtra(LevelHelper.getKEY());
        Level level = content != null ? LevelHelper.getGame().getLevel() : new Level(2);
        behavior = new SecondLevelBehavior(this, levelHelper, level);
        behavior.setStatusBarTransparent();
        behavior.startMusic();

        behavior.initViews();
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
        behavior.initGraphic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        behavior.stoptMusic();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_WRITE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            behavior.share();
        }
    }
}
