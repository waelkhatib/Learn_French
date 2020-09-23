package com.waelalk.learnfrench.behavior;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.venmo.view.TooltipView;
import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Balloon;
import com.waelalk.learnfrench.model.Level;
import com.waelalk.learnfrench.model.Translation;
import com.waelalk.learnfrench.view.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FirstLevelBehavior implements Initialization {
    private final int[] mBalloonColors = new int[3];
    private final ViewGroup mContentView;
    private final AppCompatActivity activity;
    private ViewStub overlay;
    private ViewStub web_import;
    private TooltipView tooltipView;
    private static final int MIN_ANIMATION_DELAY = 500;
    private static final int MAX_ANIMATION_DELAY = 1500;
    private static final int MIN_ANIMATION_DURATION = 1000;
    private static final int MAX_ANIMATION_DURATION = 8000;
    private static final int BALLOONS_PER_LEVEL = 10;
    private int mNextColor, mScreenWidth, mScreenHeight;
    private final TransitionDrawable trans;
    //   private final List<Balloon> mBalloons = new ArrayList<>();
    private final LevelHelper levelHelper;
    private final CountDownTimer timer;
    private ImageView imageView;
    private boolean stableStatus = false;

    public FirstLevelBehavior(AppCompatActivity activity, LevelHelper levelHelper,Level level) {
        this.activity = activity;
        this.levelHelper = levelHelper;
        LevelHelper.getGame().setLevel(level);
        mBalloonColors[0] = ContextCompat.getColor(getLevelHelper().getContext(),R.color.colorBlue);
        mBalloonColors[1] = ContextCompat.getColor(getLevelHelper().getContext(),R.color.colorWhite);
        mBalloonColors[2] = ContextCompat.getColor(getLevelHelper().getContext(),R.color.colorAccent);
        mContentView = getActivity().findViewById(R.id.rlt_layout);
        ViewTreeObserver viewTreeObserver = mContentView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mScreenHeight = mContentView.getHeight();
                    mScreenWidth = mContentView.getWidth();
                }
            });
        }
       this.trans = new TransitionDrawable(new Drawable[] {levelHelper.getResources().getDrawable(R.drawable.rectangle), levelHelper.getResources().getDrawable(R.drawable.blue_rectangle)});
        this.timer=new CountDownTimer(10000, 500) {

            public void onTick(long millisUntilFinished) {
                trans.startTransition(500);
            }

            public void onFinish() {
                trans.reverseTransition(500);
            }
        };

    }

    AppCompatActivity getActivity() {
        return activity;
    }

    LevelHelper getLevelHelper() {
        return levelHelper;
    }

    private String correctAnswer;

    void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    private View getCorrectButton() {
        return correctButton;
    }

    void setCorrectButton(View correctButton) {
        this.correctButton = correctButton;
    }

    private View correctButton=null;

    Level getLevel() {
        return LevelHelper.getGame().getLevel();
    }

    @Override
    public void startMusic() {
        levelHelper.runBackgroundMusic();
    }

    @Override
    public void stoptMusic() {
       levelHelper.getMainPlayer().pause();
    }

    @Override
    public void resumeMusic() {
       levelHelper.getMainPlayer().start();
    }

    @Override
    public void share() {
        String path=SaveBackground();
        Uri bmpUri = Uri.parse(path);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM,  bmpUri);
       activity. startActivity(Intent.createChooser(sharingIntent, "Share level"));
    }


    @Override
    public void checkAnswer(CharSequence answer) {
        levelHelper.getMainPlayer().pause();

        if(overlay==null){
            overlay = getActivity().findViewById(R.id.sub_import);
            overlay.inflate();
            web_import = getActivity().findViewById(R.id.web_import);
            View v= web_import.inflate();

            imageView=v. findViewById(R.id.emot);
            tooltipView=v.findViewById(R.id. tooltip);
        }
        overlay.setVisibility(View.VISIBLE);



        SharedPreferences.Editor editor = levelHelper.getSharedPreferences().edit();
        //String message="";
        if(answer.equals(correctAnswer)){
            getLevel().updatePoints(LevelHelper.getPointValue());

           Glide.with(getActivity()).asGif().load(R.drawable.win).into(imageView);



            if(getLevel().getQuestionNo()+1>LevelHelper.getQuestionCount()){
                stableStatus=true;
              levelHelper.startVictoryTone();
                BalloonLauncher balloonLauncher = new BalloonLauncher(this);
                balloonLauncher.execute(1);
              int levelNo=getLevel().getLevelNo();
                if(levelNo==1 || levelNo==2){
                    showTooltip(levelHelper.getString(R.string.go_to_next_level));
                    LevelHelper.getGame().setLevel(new Level(getLevel().getLevelNo()+1));
                    LevelHelper.getGame().updateGeneralLevelNo();
                }else
                if(levelNo>3){
                    showTooltip(levelHelper.getString(R.string.all_phases_finish));

                }

                Log.d("-@-",new Gson().toJson(LevelHelper.getGame()));
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
                editor.apply();
                levelHelper.makeMessageBox( 2, this);
            }else {
                getLevel().updateQuestionNo(1);
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
                editor.apply();
                levelHelper.startWinTone();
                showTooltip(levelHelper.getString(R.string.Your_answer_is_correct));

                levelHelper.makeMessageBox( 1, this);
            }
        }else {
            Glide.with(getActivity()).asGif().load(R.drawable.lose).into(imageView);


            if (getLevel().getChancesNo() > 1) {
                getLevel().updateQuestionNo(1);
                getLevel().updatePoints(-LevelHelper.getPointValue()*2);
                getLevel().updateChancesNo(-1);
                levelHelper.startFailTone();
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
                editor.apply();
                showTooltip(levelHelper.getString(R.string.Your_answer_is_wrong));
                levelHelper.  makeMessageBox(1,this);
            }else {
                stableStatus=true;
                levelHelper.startGameOverTone();
                LevelHelper.getGame().setLevel(new Level(getLevel().getLevelNo()));
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
                editor.apply();
                showTooltip(levelHelper.getString(R.string.game_over_label));

                levelHelper. makeMessageBox(2,this);
            }

        }







    }

    private void showTooltip(String message) {
        tooltipView.setText(message);
        web_import.setVisibility(View.VISIBLE);
    }


    @Override
    public void makeEffect() {
        if(getCorrectButton()!=null){
            setCardColorTran(getCorrectButton());
            timer.start();
        }
    }


    void initHeader(final Initialization initialization) {
       getActivity(). findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   // only for gingerbread and newer versions

                   if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                       // Permission is not granted
                       if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                               Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                           // No explanation needed; request the permission
                           ActivityCompat.requestPermissions(getActivity(),
                                   new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                  LevelHelper. MY_PERMISSIONS_WRITE);
                       }else
                          initialization. share();
                   }else
                       initialization.share();

               }else
                   initialization.share();
           }
       });
        switch (getLevel().getChancesNo()){
            case 2:
                getActivity().findViewById(R.id.chnc3).setVisibility(View.GONE);

                break;
            case 1:
                getActivity().findViewById(R.id.chnc3).setVisibility(View.GONE);

                getActivity().findViewById(R.id.chnc2).setVisibility(View.GONE);


                break;
            default:break;
        }
        ((TextView) getActivity().findViewById(R.id.points)).setText(String.format(Locale.ENGLISH, "%d", getLevel().getPoints()));
        ((TextView) getActivity().findViewById(R.id.status)).setText(String.format(Locale.ENGLISH, "%d/%d", getLevel().getQuestionNoForHeader(), LevelHelper.getQuestionCount()));
        if(overlay!=null){
            overlay.setVisibility(View.GONE);
            web_import.setVisibility(View.GONE);

        }

        timer.cancel();
        trans.resetTransition();
        if(getCorrectButton()!=null){
            getCorrectButton().setBackground(getLevelHelper().getResources().getDrawable(R.drawable.rectangle));
        }

    }

    @Override
    public void initViews() {
        initHeader(this);
        List<Translation> translations=LevelHelper.getDbHelper().getSpecificTranslations(LevelHelper.generateRandomOptions(4 ,getLevel().getQuestions().get(getLevel().getQuestionNo()-1)));

        ImageView imageView = getActivity().findViewById(R.id.imgView);
        Button opt_btn=null;
        for(int i=0;i<translations.size();i++){
            Translation translation=translations.get(i);
            switch (i){
                case 0:
                    opt_btn = getActivity().findViewById(R.id.option1);
                    break;
                case 1:
                    opt_btn = getActivity().findViewById(R.id.option2);
                    break;
                case 2:
                    opt_btn = getActivity().findViewById(R.id.option3);
                    break;
                case 3:
                    opt_btn = getActivity().findViewById(R.id.option4);
                    break;
            }
            opt_btn.setText(translation.getSynonym());
            if(translation.isCorrect()){
                setCorrectButton(opt_btn);
                setCorrectAnswer( translation.getSynonym());
                imageView.setImageResource(getLevelHelper(). getResources().getIdentifier("img"+translation.getId(),"drawable",getLevelHelper(). getPackageName()));

            }

        }
    }


    @Override
    public void finish() {
        if(!stableStatus)
        {
            SharedPreferences.Editor editor = levelHelper.getSharedPreferences().edit();
            LevelHelper.getGame().setLevel(new Level(getLevel().getLevelNo()));
            editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
            editor.apply();
        }
        if(activity.isTaskRoot()) {
            Intent intent=new Intent(getLevelHelper().getContext(),MainActivity.class);
            intent.putExtra("levelNo",LevelHelper.getGame().getGeneralevelNo());
            activity.startActivity(intent);
        }
        else {
            Intent intent=new Intent(getLevelHelper().getContext(),MainActivity.class);
            intent.putExtra("levelNo",LevelHelper.getGame().getGeneralevelNo());
            activity.setResult(Activity.RESULT_OK, intent);
        }

        activity.finish();

    }

    private void setCardColorTran(View view) {


            view.setBackground(trans);


    }

    private static class BalloonLauncher extends AsyncTask<Integer, Integer, Void> {
        private final WeakReference<FirstLevelBehavior> activityReference;

        // only retain a weak reference to the activity
        BalloonLauncher(FirstLevelBehavior context) {
            activityReference = new WeakReference<>(context);
        }
        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Expected 1 param for current level");
            }

            int level = params[0];
            int maxDelay = Math.max(MIN_ANIMATION_DELAY,
                    (MAX_ANIMATION_DELAY - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int balloonsLaunched = 0;
            while ( balloonsLaunched < BALLOONS_PER_LEVEL) {

//              Get a random horizontal position for the next balloon
                Random random = new Random();
                int xPosition = random.nextInt(activityReference.get().mScreenWidth - 200);
                publishProgress(xPosition);
                balloonsLaunched++;

//              Wait a random number of milliseconds before looping
                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            activityReference.get().launchBalloon(xPosition);
        }

    }

    private void launchBalloon(int x) {

        Balloon balloon = new Balloon(getLevelHelper().getContext(), mBalloonColors[mNextColor], 150);
        //     mBalloons.add(balloon);

        if (mNextColor + 1 == mBalloonColors.length) {
            mNextColor = 0;
        } else {
            mNextColor++;
        }

//      Set balloon vertical position and dimensions, add to container
        balloon.setX(x*(Locale.getDefault().getLanguage().equals("ar")?-1:1));
        balloon.setY(mScreenHeight + balloon.getHeight());
        mContentView.addView(balloon);

//      Let 'er fly
        int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION -   1000);
        balloon.releaseBalloon(mScreenHeight, duration);

    }
    public void setStatusBarTransparent() {
        Window window =getActivity(). getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0x00000000);  // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.addFlags(flags);
        }
    }

    String SaveBackground()
    {
        Bitmap bitmap;
        RelativeLayout panelResult = getActivity().findViewById(R.id.rlt_layout);
        panelResult.invalidate();
        panelResult.setDrawingCacheEnabled(true);
        panelResult.buildDrawingCache();
        DisplayMetrics displaymetrics = new DisplayMetrics();
     getActivity().   getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int i = displaymetrics.heightPixels;
        int j = displaymetrics.widthPixels;
        bitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(panelResult.getDrawingCache()), j, i, true);
        panelResult.setDrawingCacheEnabled(false);
        String s;
        File file;
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().toString()).append(File.separator).append(getLevelHelper(). getString(R.string.app_name));
        file = new File(sb.toString());
        file.isDirectory();
        file.mkdir();
        FileOutputStream fileoutputstream1 = null;
        s = (new StringBuilder(String.valueOf("guess"))).append("_sound_").append(System.currentTimeMillis()).append(".png").toString();
        try {
            fileoutputstream1 = new FileOutputStream(new File(file, s));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        FileOutputStream fileoutputstream = fileoutputstream1;

        StringBuilder stringbuilder1;
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fileoutputstream);
        stringbuilder1 = new StringBuilder();
        stringbuilder1.append(sb.toString()).append(File.separator).append(s);

        try {
            fileoutputstream.flush();
            fileoutputstream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ""+stringbuilder1;

    }
}
