package com.linky.liuhe.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;

import com.linky.liuhe.bean.LiuheBean;
import com.linky.liuhe.bean.SxYearStatisBean;
import com.linky.liuhe.bean.YearTeStatisBean;
import com.linky.liuhe.db.DbConfig;
import com.linky.liuhe.db.LiuheDbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author linky
 * @date 1/16/18
 */

public class DataUtils {

    public static Observable<Boolean> initDb(Context context) {

        return Observable.create(subscriber -> {

            try {
                moveDbFromAssetToDb(context);
                subscriber.onNext(true);
                subscriber.onCompleted();
            } catch (IOException e) {
                subscriber.onError(e);
            }

        });
    }

    public static Observable<Map<String, Integer>> getColorBallByYear(Context context, String year) {

        return Observable.create(subscriber -> {

            Map<String, Integer> map = new HashMap<>();

            String redBall = "(1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46)";
            String blueBall = "(3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48)";
            String greenBall = "(5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49)";

            LiuheDbHelper dbHelper = new LiuheDbHelper(context);
            SQLiteDatabase rDb = dbHelper.getReadableDatabase();
            String redSql = "select COUNT(*) as seasons from (select * from liuhe where season LIKE '" + year + "%') as l where te IN " + redBall;
            String blueSql = "select COUNT(*) as seasons from (select * from liuhe where season LIKE '" + year + "%') as l where te IN " + blueBall;
            String greenSql = "select COUNT(*) as seasons from (select * from liuhe where season LIKE '" + year + "%') as l where te IN " + greenBall;

            Cursor cursor = rDb.rawQuery(redSql, null);
            if (cursor.moveToNext()) {
                int seasons = cursor.getInt(cursor.getColumnIndex("seasons"));
                map.put("red", seasons);
            }

            cursor = rDb.rawQuery(blueSql, null);
            if (cursor.moveToNext()) {
                int seasons = cursor.getInt(cursor.getColumnIndex("seasons"));
                map.put("blue", seasons);
            }

            cursor = rDb.rawQuery(greenSql, null);
            if (cursor.moveToNext()) {
                int seasons = cursor.getInt(cursor.getColumnIndex("seasons"));
                map.put("green", seasons);
            }

            cursor.close();
            subscriber.onNext(map);
            subscriber.onCompleted();
        });

    }

    public static Observable<Integer> getTotalSeasons(Context context) {

        return Observable.create(subscriber -> {

            int total = 0;
            LiuheDbHelper dbHelper = new LiuheDbHelper(context);
            SQLiteDatabase rDb = dbHelper.getReadableDatabase();
            String sql = "select COUNT(*) as c from liuhe;";

            Cursor cursor = rDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                total = cursor.getInt(cursor.getColumnIndex("c"));
            }

            cursor.close();
            subscriber.onNext(total);
            subscriber.onCompleted();
        });
    }

    public static Observable<LiuheBean> getlatestNumber(Context context) {

        return Observable.create(subscriber -> {

            LiuheBean lb = new LiuheBean();

            LiuheDbHelper dbHelper = new LiuheDbHelper(context);
            SQLiteDatabase rDb = dbHelper.getReadableDatabase();
            String sql = "select * from liuhe where season = (select max(season) from liuhe);";

            Cursor cursor = rDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {

                lb.season = cursor.getString(cursor.getColumnIndex("season"));
                lb.open_time = cursor.getString(cursor.getColumnIndex("open_time"));
                lb.p1 = cursor.getInt(cursor.getColumnIndex("p1"));
                lb.p2 = cursor.getInt(cursor.getColumnIndex("p2"));
                lb.p3 = cursor.getInt(cursor.getColumnIndex("p3"));
                lb.p4 = cursor.getInt(cursor.getColumnIndex("p4"));
                lb.p5 = cursor.getInt(cursor.getColumnIndex("p5"));
                lb.p6 = cursor.getInt(cursor.getColumnIndex("p6"));
                lb.te = cursor.getInt(cursor.getColumnIndex("te"));

                String whereZodiac = "season = '" + lb.season + "'";
                Cursor c = rDb.query(DbConfig.DB_TABLE_LIUHE_SX, null, whereZodiac, null, null, null, null);
                initZodiac(c, lb);
                c.close();

            }

            cursor.close();
            subscriber.onNext(lb);
            subscriber.onCompleted();
        });
    }

    public static Observable<List<SxYearStatisBean>> getSxStatisByYear(Context context, String year) {

        return Observable.create(subscriber -> {

            List<SxYearStatisBean> beans = new ArrayList<>();

            LiuheDbHelper dbHelper = new LiuheDbHelper(context);
            SQLiteDatabase rDb = dbHelper.getReadableDatabase();
            String sql = "select te, COUNT(*) as seasons from (select * from liuhe_sx where season LIKE '" + year + "%') as l group by te order by te;";

            Cursor cursor = rDb.rawQuery(sql, null);
            while (cursor.moveToNext()) {

                SxYearStatisBean bean = new SxYearStatisBean();
                bean.tesx = cursor.getString(cursor.getColumnIndex("te"));
                bean.seasons = cursor.getInt(cursor.getColumnIndex("seasons"));
                beans.add(bean);
            }

            cursor.close();
            subscriber.onNext(beans);
            subscriber.onCompleted();
        });
    }

    public static Observable<List<YearTeStatisBean>> getStatisByYear(Context context, String year) {

        return Observable.create(subscriber -> {

            List<YearTeStatisBean> beans = new ArrayList<>();

            LiuheDbHelper dbHelper = new LiuheDbHelper(context);
            SQLiteDatabase rDb = dbHelper.getReadableDatabase();
            String sql = "select te, COUNT(*) as seasons from (select * from liuhe " +
                    "where season LIKE '" + year+ "%') as l group by te order by te;";

            Cursor cursor = rDb.rawQuery(sql, null);
            while (cursor.moveToNext()) {

                YearTeStatisBean bean = new YearTeStatisBean();
                bean.te = cursor.getInt(cursor.getColumnIndex("te"));
                bean.seasons = cursor.getInt(cursor.getColumnIndex("seasons"));
                beans.add(bean);
            }

            cursor.close();
            subscriber.onNext(beans);
            subscriber.onCompleted();
        });
    }

    public static Observable<List<LiuheBean>> loadAllDataFromDB(Context context, String year) {

        return Observable.create(subscriber -> {

            List<LiuheBean> beans = new ArrayList<>();

            LiuheDbHelper dbHelper = new LiuheDbHelper(context);
            SQLiteDatabase rDb = dbHelper.getReadableDatabase();
            String where = "season LIKE '" + year + "%'";
            Cursor cursor = rDb.query(DbConfig.DB_TABLE_LIUHE, null, where, null, null, null, null);

            while (cursor.moveToNext()) {

                LiuheBean lb = new LiuheBean();

                lb.season = cursor.getString(cursor.getColumnIndex("season"));
                lb.open_time = cursor.getString(cursor.getColumnIndex("open_time"));
                lb.p1 = cursor.getInt(cursor.getColumnIndex("p1"));
                lb.p2 = cursor.getInt(cursor.getColumnIndex("p2"));
                lb.p3 = cursor.getInt(cursor.getColumnIndex("p3"));
                lb.p4 = cursor.getInt(cursor.getColumnIndex("p4"));
                lb.p5 = cursor.getInt(cursor.getColumnIndex("p5"));
                lb.p6 = cursor.getInt(cursor.getColumnIndex("p6"));
                lb.te = cursor.getInt(cursor.getColumnIndex("te"));

                String whereZodiac = "season = '" + lb.season + "'";
                Cursor c = rDb.query(DbConfig.DB_TABLE_LIUHE_SX, null, whereZodiac, null, null, null, null);
                initZodiac(c, lb);
                c.close();

                beans.add(lb);
            }

            cursor.close();

            subscriber.onNext(beans);
            subscriber.onCompleted();
        });
    }

    private static void initZodiac(Cursor c, LiuheBean lb) {

        while (c.moveToNext()) {
            lb.zodiac1 = c.getString(c.getColumnIndex("p1"));
            lb.zodiac2 = c.getString(c.getColumnIndex("p2"));
            lb.zodiac3 = c.getString(c.getColumnIndex("p3"));
            lb.zodiac4 = c.getString(c.getColumnIndex("p4"));
            lb.zodiac5 = c.getString(c.getColumnIndex("p5"));
            lb.zodiac6 = c.getString(c.getColumnIndex("p6"));
            lb.zodiacTe = c.getString(c.getColumnIndex("te"));
        }
    }

    /**
     * 将 assets 中的 db 文件拷贝到 /data/data/*\/databases 目录下
     */
    private static void moveDbFromAssetToDb(Context context) throws IOException {

        // 创建数据库
        File dbFile = context.getDatabasePath(DbConfig.DB_NAME);

        if (dbFile.exists()) {
            return;
        }

        File dbDir = dbFile.getParentFile();
        if (!dbDir.exists()) {
            if (!dbDir.mkdir()) {
                throw new IOException("创建 db 失败");
            }
        }

        InputStream fis = context.getAssets().open(DbConfig.DB_NAME);
        FileOutputStream fos = new FileOutputStream(dbFile);

        byte[] buffer = new byte[1024];
        int count;
        while ((count = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
        }

        fos.flush();
        fis.close();
        fos.close();
    }
}
