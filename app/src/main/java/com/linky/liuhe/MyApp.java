package com.linky.liuhe;

import android.app.Application;

import com.linky.liuhe.utils.L;

/**
 * @author linky
 * @date 1/16/18
 */

public class MyApp extends Application {

    public static MyApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        L.init();
        sInstance = this;
    }
}
