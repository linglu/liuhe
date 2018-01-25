package com.linky.liuhe.bean;

import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.linky.liuhe.R;

import java.util.Arrays;

/**
 * @author linky
 */
public class LiuheBean {

    public String season;
    public String open_time;
    public int p1;
    public int p2;
    public int p3;
    public int p4;
    public int p5;
    public int p6;
    public int te;

    public String zodiac1;
    public String zodiac2;
    public String zodiac3;
    public String zodiac4;
    public String zodiac5;
    public String zodiac6;
    public String zodiacTe;

    private static Integer[] RED_BALL = {1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46};
    private static Integer[] BLUE_BALL = {3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48};
    private static Integer[] GREEN_BALL = {5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49};

    public static @DrawableRes int getBallColor(int number) {
        if (Arrays.asList(RED_BALL).contains(number)) {
            return R.drawable.red_cicle;
        }

        if (Arrays.asList(BLUE_BALL).contains(number)) {
            return R.drawable.blue_cicle;
        }

        if (Arrays.asList(GREEN_BALL).contains(number)) {
            return R.drawable.green_cicle;
        }

        throw new IllegalArgumentException("不合法的号码");
    }

    public String getOpenTime() {
        return TextUtils.isEmpty(open_time) || open_time.equals("null") ? "无" : open_time;
    }
}
