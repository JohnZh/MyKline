package com.johnzh.klinelib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

import com.johnzh.klinelib.assist.DetailView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.CandlesAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.SimpleAuxiliaryLines;
import com.johnzh.klinelib.auxiliarylines.VolAuxiliaryLines;
import com.johnzh.klinelib.date.DefaultDrawDate;
import com.johnzh.klinelib.date.DrawDate;
import com.johnzh.klinelib.gesture.DragInfo;
import com.johnzh.klinelib.gesture.Scale;
import com.johnzh.klinelib.indexes.Index;
import com.johnzh.klinelib.indexes.MAIndex;
import com.johnzh.klinelib.indexes.PureKIndex;
import com.johnzh.klinelib.indexes.VolIndex;
import com.johnzh.klinelib.size.DefaultViewSize;
import com.johnzh.klinelib.size.ViewSize;

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
        static final int MSG_SHOW_DETAIL = 1;
        static final int MSG_KEEP_SHOW_DETAIL = 2;
        static final int MSG_CANCEL_SCALE = 3;

        /**
         * Cancel detail action if the period of the touch from down to up/cancel is <= PERIOD_OF_CANCEL_DETAIL.
         * In another word, cancel detail action when touch is a click event
         */
        static final int PERIOD_OF_CANCEL_DETAIL = 100;

        static final int DELAY_FOR_SAFE = 100;

        private WeakReference<KlineView> mReference;

        public InnerHandler(KlineView view) {
            this.mReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_SHOW_DETAIL) {
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
    public static final Paint sPaint = new Paint();

    private List<? extends KlineData> mKlineDataList;

    private Index mCurIndex;
    private int mCurIndexPos;

    private Factory mFactory;
    private KlineConfig mConfig;
    private ViewSize mViewSize;
    private DrawDate mDrawDate;
    private int mCandles;
    private DrawArea mDrawArea;
    private SharedObjects mSharedObjects;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

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
        mFactory = new DefaultFactory();
        mDrawArea = new DefaultDrawArea();
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
            mHandler.sendMessageDelayed(msg, InnerHandler.DELAY_FOR_SAFE);
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

        @Override
        public void onLongPress(MotionEvent e) {
            mHandler.obtainMessage(InnerHandler.MSG_SHOW_DETAIL, e)
                    .sendToTarget();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }
    };

    private void setTouchAction(TouchAction action) {
        mAction = action;
    }

    private void triggerDetailEvent(MotionEvent e) {
        setTouchAction(TouchAction.DETAIL);
        if (mDetailView != null) {
            mDetailView.onActionDown(e);
        }
    }
// =================== Start: get / set =========================================================

    public Factory getFactory() {
        return mFactory;
    }

    public void setFactory(Factory factory) {
        mFactory = factory;
    }

    public void setConfig(@NonNull KlineConfig config) {
        if (config != null) {
            mConfig = config;
            if (mConfig.getIndexes().isEmpty()) {
                mCurIndexPos = -1;
                mCurIndex = mFactory.createDefaultIndex(PureKIndex.class);
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

    public DrawArea getDrawArea() {
        return mDrawArea;
    }

    public ViewSize getViewSize() {
        return mViewSize;
    }

    public DrawDate getDrawDate() {
        return mDrawDate;
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

    public void setViewSize(ViewSize viewSize) {
        mViewSize = viewSize;
    }

    public void setDrawDate(DrawDate drawDate) {
        mDrawDate = drawDate;
    }

    public void setDetailView(DetailView detailView) {
        mDetailView = detailView;
        mDetailView.attach(this);
    }

    // =================== End: get / set ============================================================

    class DefaultFactory implements Factory {

        @Override
        public Index createDefaultIndex(Class clazz) {
            int[] posNegColor = {
                    Color.parseColor("#f62048"),
                    Color.parseColor("#39ae13")
            };

            float dataPaddingHorizontal = toPx(TypedValue.COMPLEX_UNIT_DIP, 0.5f);

            if (clazz.isAssignableFrom(PureKIndex.class)) {
                float candleLineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 1);
                AuxiliaryLines auxiliaryLines
                        = createDefaultAuxiliaryLines(CandlesAuxiliaryLines.class);
                return new PureKIndex(auxiliaryLines, posNegColor, dataPaddingHorizontal, candleLineWidth);
            }

            if (clazz.isAssignableFrom(MAIndex.class)) {
                int[] maColors = {
                        Color.parseColor("#ffb405"),
                        Color.parseColor("#890cff")
                };
                AuxiliaryLines auxiliaryLines
                        = createDefaultAuxiliaryLines(CandlesAuxiliaryLines.class);
                PureKIndex purKIndex = (PureKIndex) createDefaultIndex(PureKIndex.class);
                float lineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 1);
                return new MAIndex(auxiliaryLines, purKIndex, lineWidth, new int[]{5, 10}, maColors);
            }

            if (clazz.isAssignableFrom(VolIndex.class)) {
                AuxiliaryLines volAuxiliaryLines = createDefaultAuxiliaryLines(VolAuxiliaryLines.class);
                return new VolIndex(volAuxiliaryLines, posNegColor, dataPaddingHorizontal);
            }

            return null;
        }

        @Override
        public AuxiliaryLines createDefaultAuxiliaryLines(Class clazz) {
            float fontSize = toPx(TypedValue.COMPLEX_UNIT_SP, 10);
            float lineWidth = toPx(TypedValue.COMPLEX_UNIT_DIP, 0.5f);
            float textMargin = toPx(TypedValue.COMPLEX_UNIT_DIP, 2);
            int color = Color.parseColor("#999999");

            if (clazz.isAssignableFrom(CandlesAuxiliaryLines.class)) {
                return new CandlesAuxiliaryLines(5, fontSize, lineWidth, textMargin, color);
            }

            if (clazz.isAssignableFrom(SimpleAuxiliaryLines.class)) {
                return new SimpleAuxiliaryLines(2, fontSize, lineWidth, textMargin, color);
            }

            if (clazz.isAssignableFrom(VolAuxiliaryLines.class)) {
                return new VolAuxiliaryLines(2, fontSize, lineWidth, textMargin, color);
            }

            return null;
        }
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

        drawAuxiliaryLines(canvas);
        drawIndex(canvas);
        drawDate(canvas);
    }

    protected void redraw() {
        invalidate();
    }

    private void drawDate(Canvas canvas) {
        mDrawDate.drawDate(this, mStartIndex, mEndIndex, canvas, sPaint);
    }

    private void drawIndex(Canvas canvas) {
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
        mCandles = (int) (mConfig.getInitialCandles() / mScale.getScale());
        mOneDataWidth = mDrawArea.calcOneDataWidth(mCandles);

        mDragInfo.setOneDataWidth(mOneDataWidth);
        mDragInfo.setMaxDraggedDataAmount(Math.max((mKlineDataList.size() - mCandles), 0));

        int dataMoved = mDragInfo.getDraggedDataAmount();
        mStartIndex = mKlineDataList.size() - mCandles < 0
                ? 0 : (mKlineDataList.size() - mCandles - dataMoved);
        int length = Math.min(mKlineDataList.size(), mCandles);
        mEndIndex = mStartIndex + length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

        switch (event.getActionMasked() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mAction == TouchAction.DETAIL) {
                    Log.d(TAG, "onTouchEvent: detail");
                    Message msg = mHandler.obtainMessage(InnerHandler.MSG_KEEP_SHOW_DETAIL, event);
                    mHandler.sendMessageDelayed(msg, InnerHandler.PERIOD_OF_CANCEL_DETAIL);
                    if (mDetailView != null) mDetailView.onActionDown(event);
                    return true;
                }

                mDragInfo.setActionDownX(event.getX());
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mAction == TouchAction.DETAIL
                        && !mHandler.hasMessages(InnerHandler.MSG_KEEP_SHOW_DETAIL)) {
                    if (mDetailView != null) mDetailView.onActionMove(event);
                    return true;
                }

                if (mAction == TouchAction.NONE || mAction == TouchAction.DRAG) {
                    float distance = Math.abs(event.getX() - mDragInfo.getActionDownX());
                    Log.d(TAG, "onTouchEvent: dis: " + distance
                            + ", oneDataWidth: " + mOneDataWidth);
                    if (distance > mOneDataWidth) {
                        mAction = TouchAction.DRAG;

                        int newDraggedDataAmount = mDragInfo.calcDraggedDataAmount(event.getX());

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
                if (mAction == TouchAction.DETAIL) {
                    if (mHandler.hasMessages(InnerHandler.MSG_KEEP_SHOW_DETAIL)) {
                        // It is considered as a click event
                        // then disable detail action and clear detail view
                        mHandler.removeMessages(InnerHandler.MSG_KEEP_SHOW_DETAIL);
                        mAction = TouchAction.NONE;
                        if (mDetailView != null) {
                            mDetailView.onActionCancel();
                        }
                    } else {
                        if (mDetailView != null) {
                            mDetailView.onActionUp(event);
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
