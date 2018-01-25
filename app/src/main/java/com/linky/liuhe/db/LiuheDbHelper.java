package com.linky.liuhe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author linky
 * @date 1/17/18
 */

public class LiuheDbHelper extends SQLiteOpenHelper {

    public LiuheDbHelper(Context context) {
        super(context, DbConfig.DB_NAME, null, DbConfig.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
