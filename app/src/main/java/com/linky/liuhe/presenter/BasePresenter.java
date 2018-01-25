package com.linky.liuhe.presenter;

import android.app.Activity;

/**
 * Created by Linky on 9/7/17.
 *
 */
public abstract class BasePresenter<V extends BaseView> {

    protected V mBaseView;
    protected Activity mActivity;

    public void attachView(V bv, Activity activity) {
        this.mBaseView = bv;
        this.mActivity = activity;

        onStart();
    }

    public void onStart() {}

    public void detachView() {
        mBaseView = null;
    }
}
