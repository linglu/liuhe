package com.linky.liuhe.api.rxsubscriber;

import android.util.Log;

import com.linky.liuhe.api.exception.ApiException;
import com.linky.liuhe.utils.L;

import rx.Subscriber;

/**
 * Created By Linky On 2017-06-01 2:06 PM
 * *
 */
abstract class ErrorSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
        // no-op
    }

    @Override
    public final void onError(Throwable e) {

        L.i("ErrorSubscriber : onError " + Log.getStackTraceString(e));

        if (e instanceof ApiException) {
            onError((ApiException) e);

            if (((ApiException) e).code == 403) {    // 判断 Token 是否失效

//                ToastUtils.showLong("token 失效，请重新登录");

                // 清空本地登录信息
//                PreferenceUtils.getInstance().clearTokenAndPermission();

                // 跳转到 登录 页面
//                Intent intent = new Intent(App.sInstance, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                App.sInstance.startActivity(intent);

            }

        } else {
            onError(new ApiException(e, Integer.MAX_VALUE));
        }
    }

    @Override
    public void onNext(T t) {
        // no-op
    }

    protected abstract void onError(ApiException e);
}
