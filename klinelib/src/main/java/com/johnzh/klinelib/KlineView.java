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
import com.johnzh.klinelib.gesture.DragInfo;
import com.johnzh.klinelib.gesture.Scale;

import java.lang.ref.WeakReference;
import java.util.List;
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

    private enum TouchAction {
        NONE,
        DETAIL,
        CLICK,
        DRAG,
        SCALE
    }

    static class InnerHandler extends Handler {
        static final int MSG_LONG_PRESS = 1;
        static final int MSG_KEEP_SHOW_DETAIL = 2;
        static final int MSG_CANCEL_SCALE = 3;

        /**
         * Cancel detail action if the period of the touch from down to up/cancel is <= PERIOD_OF_CANCEL_DETAIL.
         * In another word, cancel detail action when touch is a click event
         */
        static final int PERIOD_OF_CANCEL_DETAIL = 100;

        static final int DURATION_OF_LONG_PRESS = 500;

        static final int DELAY_FOR_AVOID_DRAG = 500;

        private WeakReference<KlineView> mReference;

        public InnerHandler(KlineView view) {
            this.mReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_LONG_PRESS) {
                KlineView klineView = mReference.get();
                if (klineView != null) {
                    MotionEvent e = (MotionEvent) msg.obj;
                    klineView.triggerDetailEvent(e);
                }
            } else if (msg.what == MSG_KEEP_SHOW_DETAIL) {
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

    private List<? extends KlineData> mKlineDataList;

    private List<DrawArea> mDrawAreaList;
    private KlineConfig mConfig;
    private SharedObjects mSharedObjects;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

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
        mDrawAreaList = new KlineFactory(getContext()).createDrawAreas();
        mSharedObjects = new SharedObjects();
        mConfig = new KlineConfig.Builder().build();
        setConfig(mConfig);

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), mOnScaleGestureListener);
        mGestureDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);

        mDragInfo = new DragInfo();
        mScale = new Scale();
        mAction = TouchAction.NONE;
        mHandler = new InnerHandler(this);
    }

    private ScaleGestureDetector.SimpleOnScaleGestureListener mOnScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        private float mPreScaleFactor = 1;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mAction = TouchAction.SCALE;
            Log.d(TAG, "onScaleBegin: ");
            return super.onScaleBegin(detector);
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

                if (mScale.getListener() != null) {
                    mScale.getListener().onScaleChanged(scaleFactor);
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
    };

    private void setTouchAction(TouchAction action) {
        mAction = action;
    }

    private void triggerDetailEvent(MotionEvent e) {
        setTouchAction(TouchAction.DETAIL);
        if (mDetailView != null) {
            mDetailView.onStart(e);
            mDetailView.onMove(e);
        }
    }
// =================== Start: get / set =========================================================

    public void setDrawAreaList(KlineFactory factory) {
        mDrawAreaList = factory.createDrawAreas();
        redraw();
    }

    public void setConfig(@NonNull KlineConfig config) {
        if (config != null) {
            mConfig = config;
            redraw();
        }
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

    public void setKlineDataList(@NonNull List<? extends KlineData> klineDataList) {
        mKlineDataList = klineDataList;
        redraw();
    }

    public List<? extends KlineData> getKlineDataList() {
        return mKlineDataList;
    }

    public void appendKlineData(@NonNull List<? extends KlineData> data) {

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
//        mDetailView = detailView;
//        mDetailView.attach(this);
    }

    // =================== End: get / set ============================================================

    public float toPx(int unit, float value) {
        return TypedValue.applyDimension(unit, value, getResources().getDisplayMetrics());
    }

// =================== End: default components ===================================================

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
        if (mKlineDataList == null || mKlineDataList.isEmpty()) {
            Log.i(TAG, "onDraw: mKlineDataList is null or empty");
            return;
        }

        if (mDrawAreaList == null || mDrawAreaList.isEmpty()) {
            Log.i(TAG, "onDraw: mDrawAreaList is null or empty");
            return;
        }

        calcVisibleCandles();

        for (DrawArea drawArea : mDrawAreaList) {
            drawArea.calculate(mKlineDataList, mStartIndex, mEndIndex);
        }

        for (DrawArea drawArea : mDrawAreaList) {
            drawArea.prepareOnDraw(this, mDrawAreaList);
        }

        for (DrawArea drawArea : mDrawAreaList) {
            drawArea.draw(this, canvas, sPaint);
        }
    }

    protected void redraw() {
        invalidate();
    }

    private void calcVisibleCandles() {
        mCandles = (int) (mConfig.getInitialCandles() / mScale.getScale());
        mOneDataWidth = calcOneDataWidth(mCandles);
        mDragInfo.setMaxDraggedDataAmount(Math.max((mKlineDataList.size() - mCandles), 0));
        int dataMoved = mDragInfo.getDraggedDataAmount();
        mStartIndex = mKlineDataList.size() - mCandles < 0
                ? 0 : (mKlineDataList.size() - mCandles - dataMoved);
        int length = Math.min(mKlineDataList.size(), mCandles);
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
                    Message msg = mHandler.obtainMessage(InnerHandler.MSG_KEEP_SHOW_DETAIL, event);
                    mHandler.sendMessageDelayed(msg, InnerHandler.PERIOD_OF_CANCEL_DETAIL);
                    if (mDetailView != null) mDetailView.onMove(event);
                    return true;
                }

                Message msg = mHandler.obtainMessage(InnerHandler.MSG_LONG_PRESS, event);
                mHandler.sendMessageDelayed(msg, InnerHandler.DURATION_OF_LONG_PRESS);

                mDragInfo.setActionDownX(event.getX());
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mAction == TouchAction.DETAIL
                        && !mHandler.hasMessages(InnerHandler.MSG_KEEP_SHOW_DETAIL)) {
                    if (mDetailView != null) mDetailView.onMove(event);
                    return true;
                }

                if (mAction == TouchAction.NONE || mAction == TouchAction.DRAG) {
                    float distance = Math.abs(event.getX() - mDragInfo.getActionDownX());
                    if (distance > mOneDataWidth) {
                        Log.d(TAG, "onTouchEvent: " + distance);
                        mAction = TouchAction.DRAG;

                        // It has been considered as drag action, cancel long press
                        if (mHandler.hasMessages(InnerHandler.MSG_LONG_PRESS)) {
                            mHandler.removeMessages(InnerHandler.MSG_LONG_PRESS);
                        }

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
                        }
                        return true;
                    }
                }
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mHandler.hasMessages(InnerHandler.MSG_LONG_PRESS)) {
                    // time is not enough to trigger long press
                    mHandler.removeMessages(InnerHandler.MSG_LONG_PRESS);
                }

                if (mAction == TouchAction.DETAIL) {
                    if (mHandler.hasMessages(InnerHandler.MSG_KEEP_SHOW_DETAIL)) {
                        // It is considered as a click event
                        // then disable detail action and clear detail view
                        mHandler.removeMessages(InnerHandler.MSG_KEEP_SHOW_DETAIL);
                        mAction = TouchAction.NONE;
                        if (mDetailView != null) {
                            mDetailView.onMove(event);
                            mDetailView.onCancel(event);
                        }
                    } else {
                        if (mDetailView != null) {
                            mDetailView.onMove(event);
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
