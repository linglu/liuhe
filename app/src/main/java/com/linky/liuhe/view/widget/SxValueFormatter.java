package com.linky.liuhe.view.widget;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * @author linky
 * @date 1/22/18
 */

public class SxValueFormatter implements IAxisValueFormatter {

    private static String[] SX = {"鼠", "牛", "虎", "龙", "兔", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int p = (int) value;
        return SX[p / 10];
    }

    public static int getIndexBySx(String sx) {

        for (int i = 0; i < SX.length; i++) {
            if (SX[i].equals(sx)) {
                return i;
            }
        }

        return 0;
    }
}
