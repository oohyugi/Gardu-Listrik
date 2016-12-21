package com.yogi.gardulistrik.api;

import rx.Subscriber;

/**
 * Created by yogi on 19/12/16.
 */
public abstract class MyObservable<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        onError(e.getMessage());
    }

    protected abstract void onError(String message);

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    protected abstract void onSuccess(T t);
}