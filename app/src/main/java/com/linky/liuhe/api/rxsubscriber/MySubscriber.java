package com.linky.liuhe.api.rxsubscriber;

import android.app.Activity;
import android.content.Context;

import com.linky.liuhe.api.exception.ApiException;
import com.linky.liuhe.view.widget.LoadingDialog;

/**
 * Created By Linky On 2017-06-01 3:58 PM
 * *
 */
abstract class MySubscriber<T> extends ErrorSubscriber<T> {

    @Override
    public void onStart() {
        if (showDialog()) {
            LoadingDialog.showDialogForLoading((Activity) activity(), showMessage(), true);
        }
    }

    @Override
    public void onNext(T t) {
        if (showDialog()) {
            LoadingDialog.cancelDialogForLoading();
        }

        onNextResult(t);
    }

    @Override
    protected final void onError(ApiException e) {
        if (showDialog())
            LoadingDialog.cancelDialogForLoading();

        onErrorOccur(e.message);
    }

    /**
     * 获取数据后的下一步操作信息
     */
    public abstract void onNextResult(T t);

    /**
     * 出错信息的向上显示
     */
    public abstract void onErrorOccur(String msg);

    /**
     * 显示加载框时的文字信息
     */
    public abstract String showMessage();

    /**
     * 是否显示加载框
     */
    public abstract boolean showDialog();

    /**
     * 显示加载框时，需要传入所对应的 Activity
     */
    public abstract Context activity();
}
