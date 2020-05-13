package com.johnzh.klinelib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.CandlesAuxiliaryLines;
import com.johnzh.klinelib.date.DefaultDrawDate;
import com.johnzh.klinelib.date.DrawDate;
import com.johnzh.klinelib.indexes.Index;
import com.johnzh.klinelib.indexes.IndexFactory;
import com.johnzh.klinelib.indexes.MAIndex;
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
    public static final String TAG = KlineView.class.getSimpleName();
    public static final Paint sPaint = new Paint();

    private List<? extends KlineData> mKlineDataList;

    private Index mCurIndex;
    private int mCurIndexPos;

    private IndexFactory mIndexFactory;
    private KlineConfig mConfig;
    private ViewSize mViewSize;
    private DrawDate mDrawDate;
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
        mIndexFactory = new DefaultIndexFactory();
        mDrawArea = new DefaultDrawArea();
        mSharedObjects = new SharedObjects();
        mConfig = new KlineConfig.Builder().build();
        setConfig(mConfig);

        mViewScale = 1;
    }

// =================== Start: get / set =========================================================


    public IndexFactory getIndexFactory() {
        return mIndexFactory;
    }

    public void setIndexFactory(IndexFactory indexFactory) {
        mIndexFactory = indexFactory;
    }

    public void setConfig(@NonNull KlineConfig config) {
        if (config != null) {
            mConfig = config;
            if (mConfig.getIndexes().isEmpty()) {
                mCurIndexPos = -1;
                mCurIndex = mIndexFactory.createDefaultIndex(PureKIndex.class);
            } else {
                selectIndex(0);
            }

            mViewSize = mConfig.getViewSize();
            if (mViewSize == null) {
                int dataHeight = (int) toPx(TypedValue.COMPLEX_UNIT_DIP, DefaultViewSize.DATA_HEIGHT_IN_DP);
                int dateHeight = (int) toPx(TypedValue.COMPLEX_UNIT_DIP, DefaultViewSize.DATE_HEIGHT_IN_DP);
                mViewSize = new DefaultViewSize(dataHeight, dateHeight);
            }

            mDrawDate = mConfig.getDrawDate();
            if (mDrawDate == null) {
                mDrawDate = getDefaultDrawDate();
            }
            redraw();
        }
    }

    public KlineConfig getConfig() {
        return mConfig;
    }

    public int getCandles() {
        return mCandles;
    }

    public Index getCurIndex() {
        return mCurIndex;
    }

    public void selectIndex(int indexOfIndexes) {
        if (mCurIndexPos == indexOfIndexes) return;
        int size = mConfig.getIndexes().size();
        if (indexOfIndexes >= 0 && indexOfIndexes < size) {
            mCurIndex = mConfig.getIndexes().get(indexOfIndexes);
            mCurIndexPos = indexOfIndexes;
            redraw();
        }
    }

    public int getCurIndexPos() {
        return mCurIndexPos;
    }

    /**
     * Real start index of data on screen
     *
     * @return Real start index of the visible data, not visible index
     */
    public int getStartDataIndex() {
        return mStartIndex;
    }

    /**
     * End index after the last data on screen
     * The real index of last data is {@link KlineView#getEndDataIndex()} - 1
     *
     * @return End index after the last visible data, not visible index
     */
    public int getEndDataIndex() {
        return mEndIndex;
    }

    public void setSharedObjects(SharedObjects sharedObjects) {
        mSharedObjects = sharedObjects;
    }

    public SharedObjects getSharedObjects() {
        return mSharedObjects;
    }

    public void setKlineDataList(@NonNull List<? extends KlineData> klineDataList) {
        mKlineDataList = klineDataList;
        redraw();
    }

    public List<? extends KlineData> getKlineDataList() {
        return mKlineDataList;
    }

    public void appendKlineData(@NonNull List<? extends KlineData> data) {

    }

    public DrawArea getDrawArea() {
        return mDrawArea;
    }

// =================== End: get / set ============================================================

    class DefaultIndexFactory implements IndexFactory {

        @Override
        public Index createDefaultIndex(Class clazz) {
            if (clazz.isAssignableFrom(PureKIndex.class)) {
                float candleWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 6);
                float candleLineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 1);
                AuxiliaryLines auxiliaryLines = getCandlesAuxiliaryLines();
                return new PureKIndex(auxiliaryLines,
                        new int[]{
                                Color.parseColor("#f62048"),
                                Color.parseColor("#39ae13")},
                        candleWidth, candleLineWidth);
            }

            if (clazz.isAssignableFrom(MAIndex.class)) {
                int[] maColors = {
                        Color.parseColor("#ffb405"),
                        Color.parseColor("#890cff")
                };
                AuxiliaryLines auxiliaryLines = getCandlesAuxiliaryLines();
                PureKIndex purKIndex = (PureKIndex) createDefaultIndex(PureKIndex.class);
                float lineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 1);
                return new MAIndex(auxiliaryLines, purKIndex, lineWidth, new int[]{ 5, 10}, maColors);
            }

            return null;
        }
    }

    public AuxiliaryLines getCandlesAuxiliaryLines() {
        float fontSize = toPx(TypedValue.COMPLEX_UNIT_SP, 10);
        float lineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 0.5f);
        float textMargin = toPx(TypedValue.COMPLEX_UNIT_DIP, 2);
        int color = Color.parseColor("#999999");
        return new CandlesAuxiliaryLines(5, fontSize, lineWidth, textMargin, color);
    }

    private DrawDate getDefaultDrawDate() {
        float fontSize = toPx(TypedValue.COMPLEX_UNIT_SP, 10);
        float textMargin = toPx(TypedValue.COMPLEX_UNIT_DIP, 2);
        int color = Color.parseColor("#999999");
        return new DefaultDrawDate(fontSize, textMargin, color);
    }

    public float toPx(int unit, float value) {
        return TypedValue.applyDimension(unit, value, getResources().getDisplayMetrics());
    }

// =================== End: default components ===================================================

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
        if (mKlineDataList == null || mKlineDataList.isEmpty()) {
            Log.i(TAG, "onDraw: mKlineDataList is null or empty");
            return;
        }

        mDrawArea.init(this, mViewSize, mCurIndex.getAuxiliaryLines());

        calcVisibleCandles();
        calcIndex();
        calcAuxiliaryLines();
        calcMaxDragDistance();

        drawAuxiliaryLines(canvas);
        drawMainData(canvas);
        drawDate(canvas);
    }

    private void calcMaxDragDistance() {
        mDistanceBetweenData = mDrawArea.getDistanceBetweenData(mCandles);
        mMaxDragDistance = Math.max((mKlineDataList.size() - mCandles) * mDistanceBetweenData, 0);
    }

    protected void redraw() {
        invalidate();
    }

    private void drawDate(Canvas canvas) {
        mDrawDate.onDraw(this, mStartIndex, mEndIndex, canvas, sPaint);
    }

    private void drawMainData(Canvas canvas) {
        mCurIndex.drawIndex(this, mStartIndex, mEndIndex, canvas, sPaint);
    }

    private void drawAuxiliaryLines(Canvas canvas) {
        mCurIndex.drawAuxiliaryLines(this, canvas, sPaint);
    }

    private void calcAuxiliaryLines() {
        mCurIndex.calcAuxiliaryLines(mKlineDataList, mStartIndex, mEndIndex);
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
