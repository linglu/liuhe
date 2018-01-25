package com.linky.liuhe.utils;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Author: Created by Real_Man.Data: 2017/2/17.
 * Des: Log日志打印封装
 * Modify By Linky :
 * 使用 Logger 进行打印日志格式化
 */

public class L {
    private L()
    {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "liuhe";

    public static void init() {
        Logger.init(TAG)                      // 如果仅仅调用 init 不传递参数，默认标签是 PRETTYLOGGER
                .methodCount(2)                 // 显示调用方法链的数量，默认是2
//              .hideThreadInfo()             // 隐藏线程信息，默认是显示
                .logLevel(LogLevel.FULL);       // 日志等级，其实就是控制是否打印，默认为 LogLevel.FULL
    }

    // 下面四个是默认tag的函数
    public static void i(String msg)
    {
        if (isDebug)
            Logger.i(msg);
    }

    public static void d(String msg)
    {
        if (isDebug)
            Logger.d(msg);
    }

    public static void e(String msg)
    {
        if (isDebug)
            Logger.e(msg);
    }

    public static void v(String msg)
    {
        if (isDebug)
            Logger.v(msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (isDebug)
            Logger.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (isDebug)
            Logger.d(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (isDebug)
            Logger.e(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isDebug)
            Logger.v(tag, msg);
    }
    
    public static void json(String jsonString) {
        if (isDebug) 
            Logger.json(jsonString);
    }
}
