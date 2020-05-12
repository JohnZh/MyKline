package com.johnzh.klinelib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.DefaultAuxiliaryLines;
import com.johnzh.klinelib.indexes.Index;
import com.johnzh.klinelib.indexes.PureKIndex;
import com.johnzh.klinelib.size.DefaultViewSize;
import com.johnzh.klinelib.size.ViewSize;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Modified by john on 2020/5/5
 * <p>
 * Description:
 */
public class KlineView extends View {

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    public static final Paint sPaint = new Paint();

    private List<? extends KlineData> mKlineDataList;

    private Index mCurIndex;
    private int mCurIndexPos;

    private KlineConfig mConfig;
    private ViewSize mViewSize;
    private AuxiliaryLines mAuxiliaryLines;
    private int mCandles;

    private DrawArea mDrawArea;
    private SharedObjects mSharedObjects;

    private int mStartIndex;
    private int mEndIndex;

    private float mViewScale;
    private float mDistanceBetweenData;
    private float mMaxDragDistance;

    public KlineView(Context context) {
        super(context);
        init();
    }

    public KlineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDrawArea = new DefaultDrawArea();
        mSharedObjects = new SharedObjects();
        mConfig = new KlineConfig();
        setConfig(mConfig);

        mViewScale = 1;
    }

    public float toPx(int unit, float value) {
        return TypedValue.applyDimension(unit, value, getResources().getDisplayMetrics());
    }

    public void setConfig(@NonNull KlineConfig config) {
        if (config != null) {
            mConfig = config;
            if (mConfig.getIndexes().isEmpty()) {
                mCurIndexPos = -1;
                mCurIndex = getDefaultIndex();
            }
            mViewSize = mConfig.getViewSize();
            if (mViewSize == null) {
                int dataHeight = (int) toPx(TypedValue.COMPLEX_UNIT_DIP, DefaultViewSize.DATA_HEIGHT_IN_DP);
                int dateHeight = (int) toPx(TypedValue.COMPLEX_UNIT_DIP, DefaultViewSize.DATE_HEIGHT_IN_DP);
                mViewSize = new DefaultViewSize(dataHeight, dateHeight);
            }
            mAuxiliaryLines = mConfig.getAuxiliaryLines();
            if (mAuxiliaryLines == null) {
                mAuxiliaryLines = getDefaultAuxiliaryLines();
            }
        }
        // TODO: 2020/5/6 redraw ??
    }

    private Index getDefaultIndex() {
        int positiveColor = Color.parseColor("#f62048");
        int negativeColor = Color.parseColor("#39ae13");
        float candleWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 6);
        float candleLineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 0.5f);
        return new PureKIndex(new int[]{positiveColor, negativeColor}, candleWidth, candleLineWidth);
    }

    private AuxiliaryLines getDefaultAuxiliaryLines() {
        float fontSize = toPx(TypedValue.COMPLEX_UNIT_SP, 10);
        float lineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 0.5f);
        float textMargin = toPx(TypedValue.COMPLEX_UNIT_DIP, 2);
        int color = Color.parseColor("#999999");
        return new DefaultAuxiliaryLines(5, fontSize, lineWidth, textMargin, color);
    }

    public void setSharedObjects(SharedObjects sharedObjects) {
        mSharedObjects = sharedObjects;
    }

    public void setKlineDataList(@NonNull List<? extends KlineData> klineDataList) {
        mKlineDataList = klineDataList;
        redraw();
    }

    public void appendKlineData(@NonNull List<? extends KlineData> data) {

    }

    public List<? extends KlineData> getKlineDataList() {
        return mKlineDataList;
    }

    public DrawArea getDrawArea() {
        return mDrawArea;
    }

    public SharedObjects getSharedObjects() {
        return mSharedObjects;
    }

    public void selectIndex(int indexPos) {
        if (mCurIndexPos == indexPos) return;
        int size = mConfig.getIndexes().size();
        if (indexPos >= 0 && indexPos < size) {
            mCurIndex = mConfig.getIndexes().get(indexPos);
            mCurIndexPos = indexPos;
            redraw();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = mViewSize.getViewWidth() > 0 ? mViewSize.getViewWidth() : width;
        height = mViewSize.getHeight() > 0 ? mViewSize.getHeight() : height;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mKlineDataList == null) return;

        mDrawArea.init(this, mViewSize, mAuxiliaryLines);

        calcVisibleCandles();
        calcIndex();
        calcAuxiliaryLines();
        calcMaxDragDistance();

        onDrawAuxiliaryLines(canvas);
        onDrawMainData(canvas);
        onDrawDate(canvas);
    }

    private void calcMaxDragDistance() {
        mDistanceBetweenData = mDrawArea.getDistanceBetweenData(mCandles);
        mMaxDragDistance = Math.max((mKlineDataList.size() - mCandles) * mDistanceBetweenData, 0);
    }

    protected void redraw() {
        invalidate();
    }

    private void onDrawDate(Canvas canvas) {

    }

    private void onDrawMainData(Canvas canvas) {
        mCurIndex.onDraw(this, mStartIndex, mEndIndex, canvas, sPaint);
    }

    private void onDrawAuxiliaryLines(Canvas canvas) {
        mAuxiliaryLines.onDrawHorizontalLines(this, canvas, sPaint);
        mAuxiliaryLines.onDrawVerticalLines(this, canvas, sPaint);
    }

    private void calcAuxiliaryLines() {
        mAuxiliaryLines.calcHorizontalLines(mKlineDataList, mCurIndex, mStartIndex, mEndIndex);
        mAuxiliaryLines.calcVerticalLines(mKlineDataList, mStartIndex, mEndIndex);
    }

    private void calcIndex() {
        mCurIndex.calcIndex(mKlineDataList, mStartIndex, mEndIndex);
    }

    private void calcVisibleCandles() {
        mCandles = (int) (mConfig.getInitialCandles() / mViewScale);
        mStartIndex = mKlineDataList.size() - mCandles < 0
                ? 0 : (mKlineDataList.size() - mCandles - getMovedCandles());
        int length = Math.min(mKlineDataList.size(), mCandles);
        mEndIndex = mStartIndex + length;
    }

    private int getMovedCandles() {
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    static class CalcTask implements Callable {

        @Override
        public Object call() throws Exception {
            return null;
        }
    }
}
