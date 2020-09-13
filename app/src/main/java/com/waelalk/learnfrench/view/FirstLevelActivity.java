package com.waelalk.learnfrench.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.behavior.FirstLevelBehavior;
import com.waelalk.learnfrench.behavior.Initialization;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;

import static com.waelalk.learnfrench.helper.LevelHelper.MY_PERMISSIONS_WRITE;

public class FirstLevelActivity extends AppCompatActivity  implements View.OnClickListener {


    private int[] viewIDs = new int[]{R.id.option1, R.id.option2, R.id.option3, R.id.option4};
    private Initialization behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long start = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_level);

        Log.d("time", "" + (System.currentTimeMillis() - start) / 1000.0);
        LevelHelper levelHelper = new LevelHelper(this);
        Intent intent = getIntent();
        String content = intent.getStringExtra(LevelHelper.getKEY());

        Level level = content != null ? LevelHelper.getGame().getLevel() : new Level(1);

        behavior = new FirstLevelBehavior(this, levelHelper, level);
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
        long start = System.currentTimeMillis();
        behavior.resumeMusic();
        Log.d("time1", "" + (System.currentTimeMillis() - start) / 1000.0);
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
