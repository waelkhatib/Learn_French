package com.waelalk.learnfrench.behavior;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;
import com.waelalk.learnfrench.model.Translation;

import java.util.List;

public class SecondLevelBehavior extends FirstLevelBehavior {
    public SecondLevelBehavior(AppCompatActivity activity, LevelHelper levelHelper, Level level) {
        super(activity, levelHelper, level);
    }

    @Override
    public void initViews() {
        initHeader();
        List<Translation> translations=getLevelHelper().getDbHelper().getSpecificTranslations(LevelHelper.generateRandomOptions(4 ,getLevel().getQuestions().get(getLevel().getQuestionNo()-1)));
        TextView txtView =(TextView)getActivity(). findViewById(R.id.txtView);

        Button opt_btn=null;
        for(int i=0;i<translations.size();i++){
            Translation translation=translations.get(i);
            switch (i){
                case 0:opt_btn=(Button)getActivity().findViewById(R.id.option1);break;
                case 1:opt_btn=(Button)getActivity().findViewById(R.id.option2);break;
                case 2:opt_btn=(Button)getActivity().findViewById(R.id.option3);break;
                case 3:opt_btn=(Button)getActivity().findViewById(R.id.option4);break;
            }
            opt_btn.setText(translation.getSynonym_ar());
            if(translation.isCorrect()){
                setCorrectButton(opt_btn);
                setCorrectAnswer( translation.getSynonym_ar());
                txtView.setText(translation.getSynonym());

            }

        }
    }

    @Override
    public void share() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, ((TextView)getActivity().findViewById(R.id.lbl)).getText().toString()+((TextView)getActivity().findViewById(R.id.txtView)).getText().toString());
        getActivity(). startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }
}
