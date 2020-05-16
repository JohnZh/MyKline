package com.johnzh.klinelib.assist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
    private PointF mFirstP;
    private PointF mCrossP;
    private RectF mRangRectF;
    private Paint mPaint;
    private int mLineColor;

    private float mDistanceX;
    private float mDistanceY;

    private boolean mDrawable;
    private boolean mStick;

    private GestureDetector mGestureDetector;

    public SimpleDetailView(Context context) {
        super(context);
        init();
    }

    public SimpleDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mFirstP = new PointF();
        mCrossP = new PointF();
        mRangRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLineColor = Color.parseColor("#000000");
    }

    public void setStick(boolean stick) {
        mStick = stick;
    }

    @Override
    public void attach(KlineView klineView) {
        mKlineView = klineView;
        mGestureDetector = new GestureDetector(mKlineView.getContext(), mSimpleOnGestureListener);
    }

    @Override
    public void onKlineViewTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        invalidate();
    }

    @Override
    public void onStart(MotionEvent event) {
        mFirstP.set(event.getX(), event.getY());
        mCrossP.set(event.getX(), event.getY());
        mDrawable = true;
    }

//    private void calcRangRectF() {
//        IndexDrawArea indexDrawArea = mKlineView.getIndexDrawArea();
//        mRangRectF.left = indexDrawArea.getDrawX(0);
//        int lastVisibleDataIndex = mKlineView.getEndIndex() - 1;
//        mRangRectF.right = indexDrawArea.getDrawX(
//                indexDrawArea.getVisibleIndex(lastVisibleDataIndex, mKlineView.getStartIndex()));
//        float maximum = mKlineView.getCurIndex().getAuxiliaryLines().getMaximum();
//        float minimum = mKlineView.getCurIndex().getAuxiliaryLines().getMinimum();
//        mRangRectF.top = indexDrawArea.getDrawY(maximum);
//        mRangRectF.bottom = indexDrawArea.getDrawY(minimum);
//    }

    @Override
    public void onCancel(MotionEvent event) {
        mFirstP.set(0, 0);
        mDrawable = false;
        mDistanceX = 0;
        mDistanceY = 0;
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mDistanceX += distanceX;
            mDistanceY += distanceY;
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        if (mKlineView == null || !mDrawable) return;

//        calcRangRectF();
//
//        float cx = calcCrossPointX();
//        float cy = calcCrossPointY();
//
//        if (cx != mCrossP.x || cy != mCrossP.y) { // redraw
//
//            IndexDrawArea indexDrawArea = mKlineView.getIndexDrawArea();
//            List<? extends KlineData> klineDataList = mKlineView.getKlineDataList();
//            int startIndex = mKlineView.getStartIndex();
//            int visibleIndex = indexDrawArea.getVisibleIndex(cx);
//            int dataIndex = indexDrawArea.getDataIndex(visibleIndex, startIndex);
//            KlineData klineData = klineDataList.get(dataIndex);
//
//            int top = indexDrawArea.getTop();
//            int bottom = indexDrawArea.getDateTop();
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeWidth(mKlineView.toPx(TypedValue.COMPLEX_UNIT_DIP, 1));
//            mPaint.setColor(mLineColor);
//            canvas.drawLine(cx, top, cx, bottom, mPaint);
//
//            mCrossP.set(cx, cy);
//        }
    }

    private float calcCrossPointY() {
        float cy = mFirstP.y + mDistanceY;
        if (cy < mRangRectF.top) {
            return mRangRectF.top;
        }
        if (cy > mRangRectF.bottom) {
            return mRangRectF.bottom;
        }
        return cy;
    }

    private float calcCrossPointX() {
        float cx = mFirstP.x + mDistanceX;

        Log.d("Temp", "calcCrossPointX f: " + mFirstP.x); // todo remove later
        Log.d("Temp", "calcCrossPointX d: " + mDistanceX); // todo remove later
        Log.d("Temp", "calcCrossPointX c: " + cx); // todo remove later

        if (cx < mRangRectF.left) {
            return mRangRectF.left;
        }
        if (cx > mRangRectF.right) {
            return mRangRectF.right;
        }
        return cx;
    }
}
