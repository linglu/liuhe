package com.linky.liuhe.view.widget;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * @author linky
 * @date 1/25/18
 */

public class CustomerValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public CustomerValueFormatter() {
        // 此处是显示数据的方式，显示整型或者小数后面小数位数自己随意确定
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // 数据前或者后可根据自己想要显示的方式添加
        return mFormat.format(value);
    }
}
