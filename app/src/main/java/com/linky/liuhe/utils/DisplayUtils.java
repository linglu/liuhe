package com.linky.liuhe.utils;

import android.content.Context;

/**
 * @author linky
 * @date 1/25/18
 */

public class DisplayUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
