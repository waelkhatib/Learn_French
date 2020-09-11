package com.waelalk.learnfrench.behavior;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FirstLevelBehavior implements Initialization {
    private int[] mBalloonColors=new int[3];
    private ToolTipsManager mToolTipsManager;;
    private String message="";
    private boolean loaded=false;
    private boolean stableStatus=false;
    private ViewStub overlay;
    private static final int MIN_ANIMATION_DELAY = 500;
    private static final int MAX_ANIMATION_DELAY = 1500;
    private static final int MIN_ANIMATION_DURATION = 1000;
    private static final int MAX_ANIMATION_DURATION = 8000;
    private static final int BALLOONS_PER_LEVEL = 10;
    private int mNextColor, mScreenWidth, mScreenHeight;
    private ViewGroup mContentView;;
    private List<Balloon> mBalloons = new ArrayList<>();
    private WebView web=null;
    public FirstLevelBehavior(AppCompatActivity activity, LevelHelper levelHelper,Level level) {
        this.activity = activity;
        this.levelHelper = levelHelper;
        LevelHelper.getGame().setLevel(level);
        mBalloonColors[0] = ContextCompat.getColor(getLevelHelper().getContext(),R.color.colorBlue);
        mBalloonColors[1] = ContextCompat.getColor(getLevelHelper().getContext(),R.color.colorWhite);
        mBalloonColors[2] = ContextCompat.getColor(getLevelHelper().getContext(),R.color.colorAccent);
        mToolTipsManager=new ToolTipsManager();
        mContentView = (ViewGroup)getActivity(). findViewById(R.id.rlt_layout);
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
    public AppCompatActivity getActivity() {
        return activity;
    }

    private AppCompatActivity activity;

    private TransitionDrawable trans;

    public LevelHelper getLevelHelper() {
        return levelHelper;
    }

    private LevelHelper levelHelper;

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    private String correctAnswer;

    private CountDownTimer timer;

    public View getCorrectButton() {
        return correctButton;
    }

    public void setCorrectButton(View correctButton) {
        this.correctButton = correctButton;
    }

    private View correctButton=null;

    public Level getLevel() {
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
    public void initGraphic() {
        if(getActivity().findViewById(R.id.emot)==null){
            Log.d("kados","-----");
            long start=System.currentTimeMillis();
            WebView webV=LevelHelper.getSharedWebView();
            Log.d("time2",""+(System.currentTimeMillis()-start)/1000.0);
            float dip = 192f;
            Resources r =getLevelHelper(). getResources();
            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.getDisplayMetrics()
            );
            RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams((int)px,(int)px);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
            ViewGroup relativeLayout=(ViewGroup)getActivity(). findViewById(R.id.rlt_layout);
            if(webV.getParent() != null) {
                ((ViewGroup)webV.getParent()).removeView(webV); // <- fix
            }
            relativeLayout.addView(webV, layoutParams);

        }
    }

    @Override
    public void checkAnswer(CharSequence answer) {
        levelHelper.getMainPlayer().pause();
        if(overlay==null){
            overlay=(ViewStub)getActivity().findViewById(R.id.sub_import);
            overlay.inflate();
        }
        overlay.setVisibility(View.VISIBLE);
        web = (WebView)getActivity(). findViewById(R.id.emot);
        web.setBackgroundColor(Color.TRANSPARENT); //for gif without background
        loaded=true;
        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("dim","s3"+url);
                if(loaded) {
                    Log.d("dim","s4");
                    view.setVisibility(View.VISIBLE);


                }
            }
        });
        web.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("dim","s1");
                if(web.getVisibility()==View.VISIBLE && loaded){
                    Log.d("dim","s2");
                    ToolTip.Builder builder = new ToolTip.Builder(levelHelper.getContext(), web, (RelativeLayout) getActivity().findViewById(R.id.rlt_layout), message, ToolTip.POSITION_ABOVE);
                    builder.setBackgroundColor(levelHelper.getResources().getColor(R.color.colorAccent));
                    mToolTipsManager.show(builder.build());
                    web.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        SharedPreferences.Editor editor = levelHelper.getSharedPreferences().edit();
        //String message="";
        if(answer.equals(correctAnswer)){
            getLevel().updatePoints(LevelHelper.getPointValue());


            if(getLevel().getQuestionNo()+1>LevelHelper.getQuestionCount()){
                stableStatus=true;
              levelHelper.startVictoryTone();
                BalloonLauncher balloonLauncher = new BalloonLauncher();
                balloonLauncher.execute(1);
              int levelNo=getLevel().getLevelNo();
                if(levelNo==1 || levelNo==2){
                    LevelHelper.getGame().setLevel(new Level(getLevel().getLevelNo()+1));
                    LevelHelper.getGame().updateGeneralLevelNo();
                    message=levelHelper.getString(R.string.go_to_next_level);
                }else
                if(levelNo>3){
                    message=levelHelper.getString(R.string.all_phases_finish);
                }
                web.loadUrl("file:///android_asset/win_html.html");
                Log.d("-@-",new Gson().toJson(LevelHelper.getGame()));
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
                editor.apply();
                levelHelper.makeMessageBox(levelHelper.getString(R.string.info), message, 2, this);
            }else {
                getLevel().updateQuestionNo(1);
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
                editor.apply();
                levelHelper.startWinTone();
                message=levelHelper.getString(R.string.Your_answer_is_correct);
                web.loadUrl("file:///android_asset/win_html.html");
                levelHelper.makeMessageBox(levelHelper.getString(R.string.info), message, 1, this);
            }
        }else {


            if (getLevel().getChancesNo() > 1) {
                getLevel().updateQuestionNo(1);
                getLevel().updatePoints(-LevelHelper.getPointValue()*2);
                getLevel().updateChancesNo(-1);
                levelHelper.startFailTone();
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
                editor.apply();
                message=levelHelper.getString(R.string.Your_answer_is_wrong);
                web.loadUrl("file:///android_asset/lose_html.html");
                levelHelper.  makeMessageBox(levelHelper.getString(R.string.warning),message,1,this);
            }else {
                stableStatus=true;
                levelHelper.startGameOverTone();
                LevelHelper.getGame().setLevel(new Level(getLevel().getLevelNo()));
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(LevelHelper.getGame()));
                editor.apply();
                message=levelHelper.getString(R.string.game_over_label);
                web.loadUrl("file:///android_asset/lose_html.html");
                levelHelper. makeMessageBox(levelHelper.getString(R.string.warning),message,2,this);
            }

        }







    }

    @Override
    public void makeEffect() {
        if(getCorrectButton()!=null){
            setCardColorTran(getCorrectButton());
            timer.start();
        }
    }




    public void initHeader(final Initialization initialization){
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
        ((TextView)getActivity().findViewById(R.id.points)).setText(""+getLevel().getPoints());
        ((TextView)getActivity().findViewById(R.id.status)).setText(""+getLevel().getQuestionNoForHeader()+"/"+LevelHelper.getQuestionCount());
        if(overlay!=null){
            overlay.setVisibility(View.GONE);
        }
       //getActivity().findViewById(R.id.overlay).setVisibility(View.GONE);
        WebView web = (WebView)getActivity(). findViewById(R.id.emot);
           if(web!=null) {
               Log.d("hiddemc", "--");
               web.loadUrl("about:blank");
               web.setVisibility(View.GONE);
               loaded = false;
           }
        timer.cancel();
        trans.resetTransition();
        if(getCorrectButton()!=null){
            getCorrectButton().setBackground(getLevelHelper().getResources().getDrawable(R.drawable.rectangle));
        }
   //     if(builder!=null)
        mToolTipsManager.dismissAll();
    }

    @Override
    public void initViews() {
        initHeader(this);
        List<Translation> translations=LevelHelper.getDbHelper().getSpecificTranslations(LevelHelper.generateRandomOptions(4 ,getLevel().getQuestions().get(getLevel().getQuestionNo()-1)));

        ImageView imageView=(ImageView)getActivity().findViewById(R.id.imgView);
        Button opt_btn=null;
        for(int i=0;i<translations.size();i++){
            Translation translation=translations.get(i);
            switch (i){
                case 0:opt_btn=(Button)getActivity().findViewById(R.id.option1);break;
                case 1:opt_btn=(Button)getActivity().findViewById(R.id.option2);break;
                case 2:opt_btn=(Button)getActivity().findViewById(R.id.option3);break;
                case 3:opt_btn=(Button)getActivity().findViewById(R.id.option4);break;
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
        if(web!=null)
          web.setVisibility(View.GONE);
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
    public void setCardColorTran(View view) {


        if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(trans);
        }else {
            view.setBackground(trans);
        }

    }

    private class BalloonLauncher extends AsyncTask<Integer, Integer, Void> {

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
                int xPosition = random.nextInt(mScreenWidth - 200);
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
            launchBalloon(xPosition);
        }

    }

    private void launchBalloon(int x) {

        Balloon balloon = new Balloon(getLevelHelper().getContext(), mBalloonColors[mNextColor], 150);
        mBalloons.add(balloon);

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
    public String SaveBackground()
    {
        Bitmap bitmap;
        RelativeLayout panelResult = (RelativeLayout)getActivity(). findViewById(R.id.rlt_layout);
        panelResult.invalidate();
        panelResult.setDrawingCacheEnabled(true);
        panelResult.buildDrawingCache();
        DisplayMetrics displaymetrics = new DisplayMetrics();
     getActivity().   getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int i = displaymetrics.heightPixels;
        int j = displaymetrics.widthPixels;
        bitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(panelResult.getDrawingCache()), j, i, true);
        panelResult.setDrawingCacheEnabled(false);
        String s = null;
        File file;
        boolean flag;
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().toString()).append(File.separator).append(getLevelHelper(). getString(R.string.app_name));
        file = new File(sb.toString());
        flag = file.isDirectory();
        s = null;
        if (flag)
        {
        }
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
