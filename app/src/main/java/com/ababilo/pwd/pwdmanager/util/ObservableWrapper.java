package com.ababilo.pwd.pwdmanager.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ababilo on 14.11.16.
 */

public class ObservableWrapper<T> {

    private Observable<T> observable;

    public ObservableWrapper(Observable<T> observable) {
        this.observable = observable;
    }

    public Observable<T> wrap() {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
