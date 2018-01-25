package com.linky.liuhe.api.rxsubscriber;

import android.content.Context;

/**
 * Created By Linky On 2017-06-01 4:02 PM
 * *
 */

public abstract class RxSubscriber<T> extends MySubscriber<T> {

    @Override
    public void onErrorOccur(String msg) {
        // no-op
    }

    @Override
    public String showMessage() {
        return "正在加载...";
    }

    @Override
    public boolean showDialog() {
        return false;
    }

    @Override
    public Context activity() {
        return null;
    }
}
