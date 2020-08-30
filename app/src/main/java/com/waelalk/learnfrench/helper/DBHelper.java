package com.waelalk.learnfrench.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.model.Translation;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "transl.db";
    String table = "words";
    String[] columnsToReturn = { "ID", "synonym","synonym_ar" };


    private Context context;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        this.context=context;
     }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        final Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.transl);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,Charset.forName("windows-1256")));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
//                text.append('\n');
            }
            br.close();
            for (String stmt:TextUtils.split(text.toString(),";")) {
                SQLiteStatement stmt1 = db.compileStatement(stmt);
                stmt1.execute();
//                db.execSQL(
//                        stmt
//                );
            }
        }catch (IOException e){

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS words");
        SQLiteStatement stmt1 = db.compileStatement("DROP TABLE IF EXISTS words");
        stmt1.execute();
        onCreate(db);
    }

    public Translation getSingleTranslate(int id) {


        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "ID= ?";
        Cursor cursor = db.query(table, columnsToReturn, selection, new String[]{""+id}, null, null, null);

        if (cursor!=null)
            cursor.moveToFirst();
        Translation translation=new Translation(cursor.getInt(0),cursor.getString(1),cursor.getString(2),true);
        cursor.close();
        return translation;
    }
    public List<Translation> getSpecificTranslations(List<Integer> IDs) {
        List<Translation> translateList = new ArrayList<Translation>();
        // Select All Query

        SQLiteDatabase db = this.getWritableDatabase();


        String selection = "ID in  ("+TextUtils.join(",",IDs)+")";
        Cursor cursor = db.query(table, columnsToReturn, selection, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // Adding contact to list
                translateList.add(new Translation(cursor.getInt(0),cursor.getString(1),cursor.getString(2),IDs.get(0)==cursor.getInt(0)));
            } while (cursor.moveToNext());
        }
        Collections.shuffle(translateList);
        // return contact list
        return translateList;
    }

}