package com.johnzh.klinelib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.johnzh.klinelib.indexes.NoIndex;
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

    private List<KlineData> mKlineDataList;

    private Index mCurIndex;
    private int mCurIndexPos;
    private AuxiliaryLines mAuxiliaryLines;
    private ViewSize mViewSize;
    private KlineConfig mConfig;

    private KlineCalc mKlineCalc;

    private int mCandles;
    private int mStartIndex;
    private int mEndIndex;
    private float mViewScale;

    public KlineView(Context context) {
        super(context);
        init();
    }

    public KlineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mConfig = new KlineConfig();
        mCurIndex = new NoIndex();
        mViewScale = 1;
        mAuxiliaryLines = new HorizontalAuxiliaryLines();

        int dataHeight = (int) toPx(TypedValue.COMPLEX_UNIT_DIP, DefaultViewSize.DATA_HEIGHT_IN_DP);
        int dateHeight = (int) toPx(TypedValue.COMPLEX_UNIT_DIP, DefaultViewSize.DATE_HEIGHT_IN_DP);
        mViewSize = new DefaultViewSize(dataHeight, dateHeight);
    }

    public float toPx(int unit, float value) {
        return TypedValue.applyDimension(unit, value, getResources().getDisplayMetrics());
    }

    public void setConfig(@NonNull KlineConfig config) {
        if (config != null) {
            mConfig = config;
            // TODO: 2020/5/6 redraw ??
        }
    }

    public void setKlineDataList(@NonNull List<KlineData> klineDataList) {
        mKlineDataList = klineDataList;
        if (!mConfig.getIndexes().isEmpty()) {
            selectIndex(mCurIndexPos);
        } else {
            calcIndex();
        }
    }

    public List<KlineData> getKlineDataList() {
        return mKlineDataList;
    }

    public void selectIndex(int indexPos) {
        if (mCurIndexPos == indexPos) return;
        int size = mConfig.getIndexes().size();
        if (indexPos >= 0 && indexPos < size) {
            mCurIndexPos = indexPos;
            mCurIndex = mConfig.getIndexes().get(indexPos);
            calcIndex();
        }
    }

    private void calcIndex() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = mViewSize.getWidth() > 0 ? mViewSize.getWidth() : width;
        height = mViewSize.getHeight() > 0 ? mViewSize.getHeight() : height;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calcVisibleDataAmt();
        calcIndexes();
        calcAuxiliaryLines();

        // setup kline calculator
        mKlineCalc.init(getWidth(), mViewSize.getDataHeight(), mViewSize.getDateHeight(),
                getPaddingLeft(), getPaddingTop(),
                getPaddingRight(), getPaddingBottom());
        mKlineCalc.calcMaxCandleWidth(mCandles);
        mKlineCalc.calcMaxDragDistance(mKlineDataList.size(), mCandles);
        mKlineCalc.setAuxiliaryLines(mAuxiliaryLines);
        mKlineCalc.setStartIndex(mStartIndex, mEndIndex);

        drawAuxiliaryLines(canvas);
        drawMainData(canvas);
        drawDate(canvas);
    }

    private void drawDate(Canvas canvas) {

    }

    private void drawMainData(Canvas canvas) {

    }

    private void drawAuxiliaryLines(Canvas canvas) {
        mAuxiliaryLines.drawHorizontalLines(mKlineCalc, canvas, sPaint);
        mAuxiliaryLines.drawVerticalLines(mKlineCalc, canvas, sPaint);
    }

    private void calcAuxiliaryLines() {
        mAuxiliaryLines.calcHorizontalLines(mKlineDataList, mCurIndex, mStartIndex, mEndIndex);
        mAuxiliaryLines.calcVerticalLines(mKlineDataList, mStartIndex, mEndIndex);
    }

    private void calcMaxTransactionX() {

    }

    private void calcMaxCandleWidth() {

    }

    private void calcIndexes() {
        mCurIndex.calcIndex(mKlineDataList, mStartIndex, mEndIndex);
    }

    private void calcVisibleDataAmt() {
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
