package com.johnzh.klinelib.assist;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.johnzh.klinelib.DrawArea;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Modified by john on 2020/5/14
 * <p>
 * Description: Just a simple sample code of DetailView implementation
 */
public class SimpleDetailView extends View implements DetailView {

    private KlineView mKlineView;

    private float mDrawX;
    private float mDrawY;
    private float mTouchX;
    private float mTouchY;
    private float mTranslateX;
    private float mTranslateY;
    private float mPreTranslateX;
    private float mPreTranslateY;

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
    public void onActionDown(MotionEvent event) {
        Log.d("Temp", "onActionDown: " + print(event)); // todo remove later
        mTouchX = event.getX();
        mTouchY = event.getY();
        mDrawable = true;
    }

    private String print(MotionEvent event) {
        return "x: " + event.getX() + ", y: " + event.getY();
    }

    @Override
    public void onActionMove(MotionEvent event) {
        Log.d("Temp", "onActionMove: " + print(event)); // todo remove later
        mTranslateX = event.getX() - mTouchX + mPreTranslateX;
        mTranslateY = event.getY() - mTouchY + mPreTranslateY;
    }

    @Override
    public void onActionUp(MotionEvent event) {
        Log.d("Temp", "onActionUp: " + print(event)); // todo remove later
        mPreTranslateX = mTranslateX;
        mPreTranslateY = mTranslateY;
    }

    @Override
    public void onActionCancel() {
        Log.d("Temp", "onActionCancel: "); // todo remove later
        mDrawable = false;
        mTouchX = 0;
        mTouchX = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mKlineView == null || !mDrawable) return;

        DrawArea drawArea = mKlineView.getDrawArea();
        List<? extends KlineData> klineDataList = mKlineView.getKlineDataList();
        int startIndex = mKlineView.getStartIndex();
        int dataIndex = drawArea.getDataIndex(drawArea.getVisibleIndex(mTouchX), startIndex);
        KlineData klineData = klineDataList.get(dataIndex);

    }
}
