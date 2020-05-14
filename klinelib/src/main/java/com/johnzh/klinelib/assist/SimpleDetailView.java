package com.johnzh.klinelib.assist;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.johnzh.klinelib.KlineView;

import androidx.annotation.Nullable;

/**
 * Modified by john on 2020/5/14
 * <p>
 * Description: Just a simple sample code of DetailView implementation
 */
public class SimpleDetailView extends View implements DetailView {

    private KlineView mKlineView;
    private float mTouchX;
    private float mTouchY;
    private boolean mDrawable;
    private boolean mStick;

    public SimpleDetailView(Context context) {
        super(context);
    }

    public SimpleDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStick(boolean stick) {
        mStick = stick;
    }

    @Override
    public void attach(KlineView klineView) {
        mKlineView = klineView;
    }

    @Override
    public void onUpdate(KlineView klineView, float touchX, float touchY) {
        mTouchX = touchX;
        mTouchY = touchY;
        mDrawable = true;
        invalidate();
    }

    @Override
    public void onClear() {
        mDrawable = false;
        mTouchX = 0;
        mTouchX = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mKlineView == null || !mDrawable) return;

    }
}
