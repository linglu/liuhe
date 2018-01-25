package com.linky.liuhe.utils;

import android.widget.Toast;

import com.linky.liuhe.MyApp;

/**
 * Created by Linky on 9/15/17.
 * 创建 Toast 显示消息
 */
public class ToastUtils {

    public static void showLong(String msg) {
        Toast.makeText(MyApp.sInstance, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String msg) {
        Toast.makeText(MyApp.sInstance, msg, Toast.LENGTH_SHORT).show();
    }
}
