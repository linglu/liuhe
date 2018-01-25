package com.linky.liuhe.api.rxsubscriber;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created By Linky On 2017-06-01 12:01 PM
 * *
 */
public class Transformer {

    public static <T> Observable.Transformer<T, T> switchSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}