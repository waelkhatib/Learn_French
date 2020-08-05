package com.waelalk.learnfrench.behavior;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;
import com.waelalk.learnfrench.model.Translation;
import com.waelalk.learnfrench.view.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class FirstLevelBehavior implements Initialization {
    public AppCompatActivity getActivity() {
        return activity;
    }

    private AppCompatActivity activity;

    public LevelHelper getLevelHelper() {
        return levelHelper;
    }

    private LevelHelper levelHelper;

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    private String correctAnswer;

    public Level getLevel() {
        return level;
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
    public void share() {
        Bitmap bitmap= BitmapFactory.decodeResource(getLevelHelper(). getResources(),getLevelHelper(). getResources().getIdentifier("img"+ getLevel().getQuestions().get(getLevel().getQuestionNo()-1),"drawable",getLevelHelper(). getPackageName()));
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/LatestShare.jpg";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path=file.getPath();
        Uri bmpUri = Uri.parse("file://"+path);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM,  bmpUri);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, ((TextView)activity.findViewById(R.id.lbl)).getText());
       activity. startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }

    @Override
    public void checkAnswer(CharSequence answer) {
        levelHelper.getMainPlayer().pause();
        getLevel().updateQuestionNo(1);
        SharedPreferences.Editor editor = levelHelper.getSharedPreferences().edit();
        if(answer.equals(correctAnswer)){
            getLevel().updatePoints(LevelHelper.getPointValue());
            if(getLevel().getQuestionNo()>LevelHelper.getQuestionCount()){
              levelHelper.startVictoryTone();
              getLevel().setLevelNo(getLevel().getLevelNo()+1);
              String message="";
              int levelNo=getLevel().getLevelNo();
                if(levelNo==2 || levelNo==3){
                    message=levelHelper.getString(R.string.go_to_next_level);
                }else
                if(levelNo>3){
                    message=levelHelper.getString(R.string.all_phases_finish);
                }
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(new Level(levelNo)));
                editor.apply();
                levelHelper.makeMessageBox(levelHelper.getString(R.string.info), message, 2, this);
            }else {
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(getLevel()));
                editor.apply();
                levelHelper.startWinTone();
                levelHelper.makeMessageBox(levelHelper.getString(R.string.info), levelHelper.getString(R.string.Your_answer_is_correct), 1, this);
            }
        }else {
            if (getLevel().getChancesNo() > 1) {
                getLevel().updatePoints(-LevelHelper.getPointValue()*2);
                getLevel().updateChancesNo(-1);
                levelHelper.startFailTone();
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(getLevel()));
                editor.apply();
                levelHelper.  makeMessageBox(levelHelper.getString(R.string.warning),levelHelper.getString(R.string.Your_answer_is_wrong),1,this);
            }else {
                levelHelper.startGameOverTone();
                editor.putString(LevelHelper.getKEY(),new Gson().toJson(new Level(getLevel().getLevelNo())));
                editor.apply();
                levelHelper. makeMessageBox(levelHelper.getString(R.string.warning),levelHelper.getString(R.string.game_over_label),2,this);
            }

        }
    }

    private Level level;

    public FirstLevelBehavior(AppCompatActivity activity, LevelHelper levelHelper, Level level) {
        this.activity = activity;
        this.levelHelper = levelHelper;
        this.level = level;
    }
    public void initHeader(){
       getActivity(). findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               share();
           }
       });
        switch (getLevel().getChancesNo()){
            case 2:
                getActivity().findViewById(R.id.chnc3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.spc3).setVisibility(View.GONE);
                break;
            case 1:
                getActivity().findViewById(R.id.chnc3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.spc3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chnc2).setVisibility(View.GONE);
                getActivity().findViewById(R.id.spc2).setVisibility(View.GONE);

                break;
            default:break;
        }
        ((TextView)getActivity().findViewById(R.id.points)).setText(""+getLevel().getPoints());
        ((TextView)getActivity().findViewById(R.id.status)).setText(""+getLevel().getQuestionNoForHeader()+"/"+LevelHelper.getQuestionCount());
    }

    @Override
    public void initViews() {
        initHeader();
        List<Translation> translations=getLevelHelper().getDbHelper().getSpecificTranslations(LevelHelper.generateRandomOptions(4 ,getLevel().getQuestions().get(getLevel().getQuestionNo()-1)));

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
                setCorrectAnswer( translation.getSynonym());
                imageView.setImageResource(getLevelHelper(). getResources().getIdentifier("img"+translation.getId(),"drawable",getLevelHelper(). getPackageName()));

            }

        }
    }


    @Override
    public void finish() {
        if(activity.isTaskRoot()) {
            Intent intent=new Intent(getLevelHelper().getContext(),MainActivity.class);
            intent.putExtra("levelNo",getLevel().getLevelNo());
            activity.startActivity(intent);
        }
        else {
            Intent intent=new Intent();
            intent.putExtra("levelNo",getLevel().getLevelNo());
            activity.setResult(Activity.RESULT_OK, intent);
        }

        activity.finish();

    }
}
