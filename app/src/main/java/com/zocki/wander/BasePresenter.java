package com.zocki.wander;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * Created by kaisheng3 on 2018/1/22.
 */
// https://juejin.im/post/5a61559051882573351a5fb6
public abstract class BasePresenter<T> {
    //View接口类型的软引用
    protected Reference<T> mViewRef;

    public void attachView(T view) {
        //建立关系
        if( mViewRef == null || mViewRef.get() == null ) {
            mViewRef = new SoftReference<T>(view);
        }
    }

    protected T getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
        }
    }
}
