package com.waelalk.learnfrench.behavior;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.EditText;
import android.widget.ImageView;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.LevelHelper;
import com.waelalk.learnfrench.model.Level;
import com.waelalk.learnfrench.model.Translation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdLevelBehavior extends FirstLevelBehavior {

    public ThirdLevelBehavior(AppCompatActivity activity, LevelHelper levelHelper, Level level) {
        super(activity, levelHelper, level);
    }

    @Override
    public void initViews() {
        initHeader(this);

        ImageView imageView = getActivity().findViewById(R.id.imgView);
        ((EditText)getActivity(). findViewById(R.id.input_txt)).setText("");
        Translation translation = LevelHelper.getDbHelper().getSingleTranslate(getLevel().getQuestions().get(getLevel().getQuestionNo() - 1));
        if (translation.isCorrect()) {
                setCorrectAnswer(translation.getSynonym());
                imageView.setImageResource(getLevelHelper().getResources().getIdentifier("img" + translation.getId(), "drawable", getLevelHelper().getPackageName()));

            }

    }
    public int getMediaID(){
        return getLevel().getQuestions().get(getLevel().getQuestionNo() - 1);
    }

    @Override
    public void share() {
        ArrayList<Uri> uris = new ArrayList<>();
        String path=SaveBackground();
        File dest = Environment.getExternalStorageDirectory();
        String SoundFile="w"+getMediaID();
        InputStream in =getLevelHelper(). getResources().openRawResource(getLevelHelper().getResources().getIdentifier(SoundFile,"raw",getLevelHelper().getPackageName()));
        try
        {
            OutputStream out = new FileOutputStream(new File(dest, SoundFile+".mp3"));
            byte[] buf = new byte[1024];
            int len;
            while ( (len = in.read(buf, 0, buf.length)) != -1)
            {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception ignored) {
        }

        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
        share.setType("*/*");
        uris.add(Uri.parse(path));
        uris.add(Uri.parse(Environment.getExternalStorageDirectory().toString() + "/"+SoundFile+".mp3"));
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        getActivity().startActivity(Intent.createChooser(share, "Share level"));
    }
}
