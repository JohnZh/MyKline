package com.johnzh.klinelib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.johnzh.klinelib.detail.DetailView;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.gesture.DragInfo;
import com.johnzh.klinelib.gesture.Scale;
import com.johnzh.klinelib.indicators.Indicator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Modified by john on 2020/5/5
 * <p>
 * Description:
 */
public class KlineView extends View {
    // TODO: 2020/5/17 asynchronous task to calculate indexes. Is it necessary?
    //public static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private enum TouchAction {
        NONE,
        DETAIL,
        CLICK,
        DRAG,
        SCALE
    }

    static class InnerHandler extends Handler {
        static final int MSG_ACTIVE_DETAIL = 1;
        static final int MSG_CANCEL_SCALE = 2;
        static final int MSG_CANCEL_DETAIL_WITH_ACTION_UP = 3;

        /**
         * Cancel detail action if the period of the touch from down to up/cancel is <= PERIOD_OF_CANCEL_DETAIL.
         * In another word, cancel detail action when touch is a click event
         */
        static final int DURATION_OF_ON_CLICK = 200;

        static final int DELAY_FOR_AVOID_DRAG = 500;

        private WeakReference<KlineView> mReference;

        public InnerHandler(KlineView view) {
            this.mReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_ACTIVE_DETAIL) {
                KlineView klineView = mReference.get();
                if (klineView != null) {
                    MotionEvent e = (MotionEvent) msg.obj;
                    klineView.triggerDetailEvent(e);
                }
            } else if (msg.what == MSG_CANCEL_DETAIL_WITH_ACTION_UP) {
                // do nothing, just continue detail action
            } else if (msg.what == MSG_CANCEL_SCALE) {
                KlineView klineView = mReference.get();
                if (klineView != null) {
                    klineView.setTouchAction(TouchAction.NONE);
                    Log.d(TAG, "onScaleEnd: ");
                }
            }
        }
    }

    public static final String TAG = KlineView.class.getSimpleName();
    public static final Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<DATA> mDataList;
    private List<DATA> mBufferedList; // Saving the new data when dragging or in the right-most

    private List<DrawArea> mDrawAreaList;
    private KlineConfig mConfig;
    private SharedObjects mSharedObjects;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private DragInfo.Listener mOnDataDragListener;
    private Scale.Listener mOnScaleListener;

    private int mCandles;
    private int mStartIndex;
    private int mEndIndex;
    private float mOneDataWidth;

    private DragInfo mDragInfo;
    private Scale mScale;
    private TouchAction mAction;
    private Handler mHandler;
    private DetailView mDetailView;

    public KlineView(Context context) {
        super(context);
        init();
    }

    public KlineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBufferedList = new ArrayList<>();
        mDataList = new ArrayList<>();
        mDrawAreaList = new DefaultFactory(getContext()).createDrawAreaList();
        mSharedObjects = new SharedObjects();
        mConfig = new KlineConfig.Builder().build();
        setConfig(mConfig);

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), mOnScaleGestureListener);
        mGestureDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);

        mDragInfo = new DragInfo();
        mAction = TouchAction.NONE;
        mHandler = new InnerHandler(this);
    }

    private ScaleGestureDetector.SimpleOnScaleGestureListener mOnScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        private float mPreScaleFactor = 1;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            if (mAction == TouchAction.NONE) {
                mAction = TouchAction.SCALE;
                Log.d(TAG, "onScaleBegin: ");
                return true;
            }
            return false;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Message msg = mHandler.obtainMessage(InnerHandler.MSG_CANCEL_SCALE);
            mHandler.sendMessageDelayed(msg, InnerHandler.DELAY_FOR_AVOID_DRAG);
            super.onScaleEnd(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor() * mPreScaleFactor;
            if (scaleFactor > mScale.getMaxScale()) {
                scaleFactor = mScale.getMaxScale();
            }
            if (scaleFactor < mScale.getMinScale()) {
                scaleFactor = mScale.getMinScale();
            }

            if (mPreScaleFactor != scaleFactor) {
                mScale.setScale(scaleFactor);
                mPreScaleFactor = scaleFactor;

                if (mOnScaleListener != null) {
                    mOnScaleListener.onScaleChanged(scaleFactor);
                }

                redraw();
                return true;
            }
            return false;
        }
    };

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (mDetailView != null
                    && mConfig.getActiveDetailAction() == DetailView.TRIGGERED_BY_LONG_PRESS) {
                mHandler.obtainMessage(InnerHandler.MSG_ACTIVE_DETAIL, e).sendToTarget();
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mDetailView != null
                    && mConfig.getActiveDetailAction() == DetailView.TRIGGERED_BY_DOUBLE_TAP) {
                mHandler.obtainMessage(InnerHandler.MSG_ACTIVE_DETAIL, e).sendToTarget();
            }
            return super.onDoubleTap(e);
        }
    };

    private void setTouchAction(TouchAction action) {
        mAction = action;
    }

    private void triggerDetailEvent(MotionEvent e) {
        setTouchAction(TouchAction.DETAIL);
        if (mDetailView != null) {
            mDetailView.onStart(e);
        }
    }

    // =================== Start: get / set =========================================================

    /**
     * Select a indicator from a specific IndexDrawArea, and redraw KlineView
     *
     * @param drawAreaIndex
     * @param indicatorIndex
     */
    public void selectIndicator(int drawAreaIndex, int indicatorIndex) {
        if (drawAreaIndex >= 0 && drawAreaIndex < mDrawAreaList.size()) {
            DrawArea drawArea = mDrawAreaList.get(drawAreaIndex);
            if (drawArea instanceof IndicatorDrawArea) {
                IndicatorDrawArea indicatorDrawArea = (IndicatorDrawArea) drawArea;
                indicatorDrawArea.selectIndicator(indicatorIndex);
                redraw();
            }
        }
    }

    /**
     * Select indicator from all drewArea
     *
     * @param clazz
     */
    public void selectIndicator(Class<? extends Indicator> clazz) {
        boolean redraw = false;
        for (DrawArea drawArea : mDrawAreaList) {
            if (drawArea instanceof IndicatorDrawArea) {
                IndicatorDrawArea indicatorDrawArea = (IndicatorDrawArea) drawArea;
                List<Indicator> indicatorList = indicatorDrawArea.getIndicatorList();
                for (int i = 0; i < indicatorList.size(); i++) {
                    if (indicatorList.get(i).getClass() == clazz) {
                        indicatorDrawArea.selectIndicator(i);
                        redraw = true;
                    }
                }
            }
        }
        if (redraw) redraw();
    }

    public void setConfig(@NonNull KlineConfig config) {
        if (config != null) {
            mConfig = config;
            mScale = mConfig.getScale();
            if (mScale == null) {
                mScale = new Scale();
            }
            redraw();
        }
    }

    public List<DrawArea> getDrawAreaList() {
        return mDrawAreaList;
    }

    public DrawArea getDrawArea(int drawAreaIndex) {
        if (drawAreaIndex >= 0 && drawAreaIndex < mDrawAreaList.size()) {
            return mDrawAreaList.get(drawAreaIndex);
        }
        return null;
    }

    public <T extends DrawArea> List<T> getDrawAreaList(Class<T> clazz) {
        List<DrawArea> list = new ArrayList<>();
        for (DrawArea drawArea : mDrawAreaList) {
            if (drawArea.getClass() == clazz) {
                list.add(drawArea);
            }
        }
        return (List<T>) list;
    }

    public void setDrawAreaList(Factory factory) {
        mDrawAreaList = factory.createDrawAreaList();
        redraw();
    }

    public <T extends Indicator> List<T> getIndicator(Class<T> clazz) {
        List<Indicator> list = new ArrayList<>();
        for (DrawArea drawArea : mDrawAreaList) {
            if (drawArea instanceof IndicatorDrawArea) {
                IndicatorDrawArea area = (IndicatorDrawArea) drawArea;
                for (Indicator indicator : area.getIndicatorList()) {
                    if (clazz == indicator.getClass()) {
                        list.add(indicator);
                    }
                }
            }
        }
        return (List<T>) list;
    }

    public KlineConfig getConfig() {
        return mConfig;
    }

    public int getCandles() {
        return mCandles;
    }

    /**
     * Real start index of data on screen
     *
     * @return Real start index of the visible data, not visible index
     */
    public int getStartIndex() {
        return mStartIndex;
    }

    /**
     * End index after the last data on screen
     * The real index of last data is {@link KlineView#getEndIndex()} - 1
     *
     * @return End index after the last visible data, not visible index
     */
    public int getEndIndex() {
        return mEndIndex;
    }

    public void setSharedObjects(SharedObjects sharedObjects) {
        mSharedObjects = sharedObjects;
    }

    public SharedObjects getSharedObjects() {
        return mSharedObjects;
    }

    public void setOnDataDragListener(DragInfo.Listener onDataDragListener) {
        mOnDataDragListener = onDataDragListener;
    }

    public void setOnScaleListener(Scale.Listener onScaleListener) {
        mOnScaleListener = onScaleListener;
    }

    /**
     * Add list to buffered list, and refresh data when dragged to the right-most
     *
     * @param dataList this list will be add to buffered list
     */
    public void setBufferedList(@NonNull List<DATA> dataList) {
        if (!mBufferedList.isEmpty()) {
            mBufferedList.clear();
        }
        if (mDragInfo.isRightMost()) {
            setDataList(dataList);
        } else {
            mBufferedList.addAll(dataList);
            Log.d(TAG, "setBufferedList: ");
        }
    }

    public void setDataList(@NonNull List<DATA> dataList) {
        clear();
        mDataList.addAll(dataList);
        redraw();
    }

    public void clear() {
        mBufferedList.clear();
        clearExceptBuffer();
    }

    private void clearExceptBuffer() {
        mDataList.clear();
        mStartIndex = 0;
        mEndIndex = 0;
        mCandles = 0;
        mOneDataWidth = 0;
        mDragInfo.clear();
    }

    public List<DATA> getDataList() {
        return mDataList;
    }

    public void addHistoricalData(@NonNull List<DATA> dataList) {
        if (mDataList != null) {
            mDataList.addAll(0, dataList);
            redraw();
        }
    }

    public float getOneDataWidth() {
        return mOneDataWidth;
    }

    public TouchAction getAction() {
        return mAction;
    }

    public DetailView getDetailView() {
        return mDetailView;
    }

    public void setDetailView(DetailView detailView) {
        mDetailView = detailView;
        if (mDetailView != null) {
            mDetailView.attach(this);
        }
    }

    public Scale getScale() {
        return mScale;
    }

    public DragInfo getDragInfo() {
        return mDragInfo;
    }

    // =================== End: get / set ============================================================
    public float toPx(int unit, float value) {
        return TypedValue.applyDimension(unit, value, getResources().getDisplayMetrics());
    }

    public float sp2Px(float value) {
        return toPx(TypedValue.COMPLEX_UNIT_SP, value);
    }

    public float dp2Px(float value) {
        return toPx(TypedValue.COMPLEX_UNIT_DIP, value);
    }
    // =================== End: tool methods =========================================================

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = measureWidth(mDrawAreaList, width);
        height = measureHeight(mDrawAreaList, height);
        setMeasuredDimension(width, height);
    }

    protected int measureWidth(List<DrawArea> drawAreaList, int width) {
        if (getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
            return width;
        } else {
            int result = 0;
            for (DrawArea drawArea : drawAreaList) {
                if (drawArea.getWidth() < 0) { // one of drawArea width match parent
                    return width;
                } else {
                    result = Math.max(result, drawArea.getWidth());
                }
            }
            return result + getPaddingLeft() + getPaddingRight();
        }
    }

    protected int measureHeight(List<DrawArea> drawAreaList, int height) {
        if (getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT) {
            return height;
        } else {
            int result = 0;
            for (DrawArea drawArea : drawAreaList) {
                if (drawArea.getHeight() < 0) { // one of drawArea height match parent
                    return height;
                } else {
                    result += drawArea.getHeight();
                }
            }
            return result + getPaddingTop() + getPaddingBottom();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDataList == null || mDataList.isEmpty()) {
            Log.i(TAG, "onDraw: mKlineDataList is null or empty");
            return;
        }

        if (mDrawAreaList == null || mDrawAreaList.isEmpty()) {
            Log.i(TAG, "onDraw: mDrawAreaList is null or empty");
            return;
        }

        if (mAction == TouchAction.DETAIL && mDetailView != null) {
            mDetailView.onPreKlineViewDraw(this);
        }

        calcVisibleCandles();

        for (DrawArea drawArea : mDrawAreaList) {
            drawArea.calculate(mDataList, mStartIndex, mEndIndex);
        }

        for (DrawArea drawArea : mDrawAreaList) {
            drawArea.prepareOnDraw(this, mDrawAreaList);
        }

        for (DrawArea drawArea : mDrawAreaList) {
            drawArea.draw(this, canvas, sPaint);
        }

        if (mAction == TouchAction.DETAIL && mDetailView != null) {
            mDetailView.onPostKlineViewDraw(this);
        }
    }

    public void redraw() {
        invalidate();
    }

    private void calcVisibleCandles() {
        mCandles = (int) (mConfig.getInitialCandles() / mScale.getScale());
        mOneDataWidth = calcOneDataWidth(mCandles);
        mDragInfo.setMaxDraggedDataAmount(Math.max((mDataList.size() - mCandles), 0));
        int dataMoved = mDragInfo.getDraggedDataAmount();
        mStartIndex = mDataList.size() - mCandles < 0
                ? 0 : (mDataList.size() - mCandles - dataMoved);
        int length = Math.min(mDataList.size(), mCandles);
        mEndIndex = mStartIndex + length;
    }

    protected float calcOneDataWidth(int candles) {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        return width * 1.0f / candles;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mAction == TouchAction.DETAIL) {
                    Log.d(TAG, "onTouchEvent: detail");

                    Message msg = mHandler.obtainMessage(
                            InnerHandler.MSG_CANCEL_DETAIL_WITH_ACTION_UP, event);
                    mHandler.sendMessageDelayed(msg, InnerHandler.DURATION_OF_ON_CLICK);

                    if (mDetailView != null) mDetailView.onDownAgain(event);
                    return true;
                }

                mDragInfo.setActionDownX(event.getX());
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mAction == TouchAction.DETAIL
                        && !mHandler.hasMessages(InnerHandler.MSG_CANCEL_DETAIL_WITH_ACTION_UP)) {
                    if (mDetailView != null) mDetailView.onMove(event);
                    return true;
                }

                if (mAction == TouchAction.NONE || mAction == TouchAction.DRAG) {
                    float distance = Math.abs(event.getX() - mDragInfo.getActionDownX());
                    if (mOneDataWidth != 0 && distance > mOneDataWidth) {
                        mAction = TouchAction.DRAG;

                        int newDraggedDataAmount = mDragInfo
                                .calcDraggedDataAmount(event.getX(), mOneDataWidth);

//                        Log.d(TAG, "onTouchEvent: 1 dragD: " + mDragInfo.getDraggedDataAmount());
//                        Log.d(TAG, "onTouchEvent: 2 NewDragD: " + newDraggedDataAmount);
//                        Log.d(TAG, "onTouchEvent: 3 maxDragD: " + mDragInfo.getMaxDraggedDataAmount());
//                        Log.d(TAG, "onTouchEvent: 4 preDragD: " + mDragInfo.getPreDraggedDataAmount());

                        if (newDraggedDataAmount != mDragInfo.getDraggedDataAmount()) {
                            mDragInfo.setDraggedDataAmount(newDraggedDataAmount);

                            Log.d(TAG, "onTouchEvent: redraw");

                            redraw();

                            if (mOnDataDragListener != null) {
                                int remainingData = mDragInfo.getMaxDraggedDataAmount()
                                        - mDragInfo.getDraggedDataAmount();
                                mOnDataDragListener.onDrag(remainingData,
                                        mDragInfo.getDraggedDataAmount(), mCandles);
                            }

                            if (mDragInfo.isRightMost() && !mBufferedList.isEmpty()) {
                                Log.d(TAG, "refresh: " + mBufferedList.size());
                                clearExceptBuffer();
                                mDataList.addAll(mBufferedList);
                                mBufferedList.clear();
                                redraw();
                            }
                        }
                        return true;
                    }
                }
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mAction == TouchAction.DETAIL) {
                    if (mHandler.hasMessages(InnerHandler.MSG_CANCEL_DETAIL_WITH_ACTION_UP)) {
                        // It is considered as a click event
                        // then cancel detail feature
                        mHandler.removeMessages(InnerHandler.MSG_CANCEL_DETAIL_WITH_ACTION_UP);
                        mAction = TouchAction.NONE;
                        if (mDetailView != null) {
                            mDetailView.onCancel(event);
                        }
                    } else {
                        if (mDetailView != null) {
                            mDetailView.onUp(event);
                        }
                    }
                    return true;
                }

                if (mAction == TouchAction.DRAG) {
                    mAction = TouchAction.NONE;
                    mDragInfo.setPreDraggedDataAmount(mDragInfo.getDraggedDataAmount());
                    return true;
                }
        }
        return super.onTouchEvent(event);
    }

//    static class CalcTask implements Callable {
//
//        @Override
//        public Object call() throws Exception {
//            return null;
//        }
//    }
}
