package com.johnzh.klinelib;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Modified by john on 2020/5/10
 * <p>
 * Description:
 */
public class SharedObjects {

    private Path mPath;
    private StringBuilder mStringBuilder;
    private RectF mRectF;

    public SharedObjects() {
        mPath = new Path();
        mStringBuilder = new StringBuilder();
        mRectF = new RectF();
    }

    public <T> T getObject(Class<T> clazz) {
        if (clazz.isInstance(mPath)) {
            mPath.reset();
            return (T) mPath;
        }

        if (clazz.isInstance(mStringBuilder)) {
            mStringBuilder.setLength(0);
            return (T) mStringBuilder;
        }

        if (clazz.isInstance(mRectF)) {
            mRectF.setEmpty();
            return (T) mRectF;
        }
        return null;
    }
}
