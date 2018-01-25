package com.linky.liuhe.api.exception;

import rx.Observable;
import rx.functions.Func1;

public class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable throwable) {
            // ExceptionEngine 为处理异常的驱动器
            return Observable.error(ExceptionEngine.handleException(throwable));
        }
    }