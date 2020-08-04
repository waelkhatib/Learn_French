package com.waelalk.learnfrench.behavior;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;
import com.waelalk.learnfrench.model.Translation;

public class ThirdLevelBehavior extends FirstLevelBehavior {

    public ThirdLevelBehavior(AppCompatActivity activity, LevelHelper levelHelper, Level level) {
        super(activity, levelHelper, level);
    }

    @Override
    public void initViews() {
        initHeader();
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.imgView);
        ((EditText)getActivity(). findViewById(R.id.input_txt)).setText("");
        Translation translation = getLevelHelper().getDbHelper().getSingleTranslate(getLevel().getQuestions().get(getLevel().getQuestionNo() - 1));
        if (translation.isCorrect()) {
                setCorrectAnswer(translation.getSynonym());
                imageView.setImageResource(getLevelHelper().getResources().getIdentifier("img" + translation.getId(), "drawable", getLevelHelper().getPackageName()));

            }

    }
    public int getMediaID(){
        return getLevel().getQuestions().get(getLevel().getQuestionNo() - 1);
    }
}
