package com.johnzh.klinelib.detail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.DrawTextTool;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.element.SimpleDrawDate;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.DateDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorTextDrawArea;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by john on 2020/5/14
 * <p>
 * Description: Just a simple sample code of DetailView implementation
 */
public class SimpleDetailView extends View implements DetailView {

    static class PointInfo {
        private Rect rect;
        private PointF firstP;
        private PointF downP;
        private PointF crossP;

        private float distanceX;
        private float distanceY;
        private float preDistanceX;
        private float preDistanceY;

        public PointInfo() {
            rect = new Rect();
            firstP = new PointF();
            downP = new PointF();
            crossP = new PointF();
        }

        public void setDownP(MotionEvent event) {
            downP.set(event.getX(), event.getY());
        }

        public void setFirstP(MotionEvent event) {
            this.firstP.set(event.getX(), event.getY());
        }

        public void reset() {
            rect.setEmpty();
            distanceX = 0;
            preDistanceX = 0;
            distanceY = 0;
            preDistanceY = 0;
        }

        public void updateDistance(MotionEvent event) {
            distanceX = event.getX() - downP.x + preDistanceX;
            distanceY = event.getY() - downP.y + preDistanceY;

            distanceX = fixDistanceX(distanceX);
            distanceY = fixDistanceY(distanceY);
        }

        private float fixDistanceX(float distanceX) {
            float cx = firstP.x + distanceX;
            if (cx < rect.left) distanceX -= cx - rect.left;
            if (cx > rect.right) distanceX -= cx - rect.right;
            return distanceX;
        }

        private float fixDistanceY(float distanceY) {
            float cy = firstP.y + distanceY;
            if (cy < rect.top) distanceY -= cy - rect.top;
            if (cy > rect.bottom) distanceY -= cy - rect.bottom;
            return distanceY;
        }

        public void saveDistance() {
            preDistanceX = distanceX;
            preDistanceY = distanceY;
        }

        public PointF getCrossP() {
            crossP.x = firstP.x + distanceX;
            crossP.y = firstP.y + distanceY;
            return crossP;
        }
    }

    static class CrossPointInfo {
        private float y;
        private int visibleIndex;

        public void updateInfo(float y, int visibleIndex) {
            this.y = y;
            this.visibleIndex = visibleIndex;
        }

        public void reset() {
            this.y = 0;
            this.visibleIndex = -1;
        }
    }

    private static final int FONT_SIZE = 10;
    private static final int TEXT_PADDING = 2;

    private KlineView mKlineView;
    private DrawArea mDrawArea;
    private List<DrawArea> mIndicatorTextDrawAreaList;

    private Paint mPaint;
    private RectF mBgRectF;
    private int mLineColor;
    private int mTextColor;

    private PointInfo mPointInfo;
    private CrossPointInfo mCrossPointInfo;
    private boolean mStarted;

    public SimpleDetailView(Context context) {
        super(context);
        init();
    }

    public SimpleDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPointInfo = new PointInfo();
        mCrossPointInfo = new CrossPointInfo();

        mBgRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLineColor = Color.parseColor("#000000");
        mTextColor = Color.parseColor("#ffffff");
        mIndicatorTextDrawAreaList = new ArrayList<>();
    }

    @Override
    public void attach(KlineView klineView) {
        mKlineView = klineView;
    }

    @Override
    public void onMove(MotionEvent event) {
        mPointInfo.updateDistance(event);

        checkAndRedraw();
    }

    private void checkAndRedraw() {
        PointF crossP = mPointInfo.getCrossP();
        mDrawArea = getDrawArea(crossP);
        if (mDrawArea != null) {
            int newVisibleIndex = mDrawArea.getVisibleIndex(crossP.x);
            if (newVisibleIndex < 0) return;
            if (mCrossPointInfo.y != crossP.y || mCrossPointInfo.visibleIndex != newVisibleIndex) {
                mCrossPointInfo.updateInfo(crossP.y, newVisibleIndex);
                invalidate();
            }
        }
    }

    private DrawArea getDrawArea(PointF p) {
        List<DrawArea> drawAreaList = mKlineView.getDrawAreaList();
        for (DrawArea drawArea : drawAreaList) {
            if (drawArea.contains(p.x, p.y)) {
                return drawArea;
            }
        }
        return null;
    }

    @Override
    public void onDownAgain(MotionEvent event) {
        mPointInfo.setDownP(event);
    }

    @Override
    public void onUp(MotionEvent event) {
        mPointInfo.saveDistance();
    }

    @Override
    public void onStart(MotionEvent event) {
        getRangRectF(mPointInfo.rect);
        mPointInfo.setFirstP(event);
        mPointInfo.setDownP(event);
        mStarted = true;

        checkAndRedraw();

        hideKlineViewIndicatorText();
    }

    private void hideKlineViewIndicatorText() {
        List<DrawArea> drawAreaList = mKlineView.getDrawAreaList();
        for (DrawArea drawArea : drawAreaList) {
            if (drawArea instanceof IndicatorTextDrawArea) {
                ((IndicatorTextDrawArea) drawArea).setDetailActivated(true);
                mIndicatorTextDrawAreaList.add(drawArea);
            }
        }
        if (!mIndicatorTextDrawAreaList.isEmpty()) {
            mKlineView.redraw();
        }
    }

    private void getRangRectF(Rect rect) {
        List<DrawArea> drawAreaList = mKlineView.getDrawAreaList();
        rect.left = rect.top = Integer.MAX_VALUE;
        rect.right = rect.bottom = Integer.MIN_VALUE;
        for (DrawArea drawArea : drawAreaList) {
            rect.left = Math.min(rect.left, drawArea.getLeft());
            rect.top = Math.min(rect.top, drawArea.getTop());
            rect.right = Math.max(rect.right, drawArea.getLeft() + drawArea.getWidth());
            rect.bottom = Math.max(rect.bottom, drawArea.getTop() + drawArea.getHeight());
        }
    }

    @Override
    public void onCancel(MotionEvent event) {
        mStarted = false;
        mPointInfo.reset();
        mCrossPointInfo.reset();

        showKlineViewIndicatorText();

        invalidate();
    }

    private void showKlineViewIndicatorText() {
        if (!mIndicatorTextDrawAreaList.isEmpty()) {
            for (DrawArea drawArea : mIndicatorTextDrawAreaList) {
                if (drawArea instanceof IndicatorTextDrawArea) {
                    ((IndicatorTextDrawArea) drawArea).setDetailActivated(false);
                }
            }
            mIndicatorTextDrawAreaList.clear();
            mKlineView.redraw();
        }
    }

    @Override
    public void onPreKlineViewDraw(KlineView view) {

    }

    @Override
    public void onPostKlineViewDraw(KlineView view) {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mKlineView == null || mDrawArea == null || !mStarted) return;
        List<DATA> klineDataList = mKlineView.getDataList();
        int dataIndex = mDrawArea.getDataIndex(mCrossPointInfo.visibleIndex);
        DATA data = klineDataList.get(dataIndex);
        float cx = mDrawArea.getDrawX(mCrossPointInfo.visibleIndex);
        float cy = mCrossPointInfo.y;

        int top = mPointInfo.rect.top;
        int bottom = mPointInfo.rect.bottom;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mKlineView.toPx(TypedValue.COMPLEX_UNIT_DIP, 1));
        mPaint.setColor(mLineColor);
        canvas.drawLine(cx, top, cx, bottom, mPaint); // horizontal line

        int left = mPointInfo.rect.left;
        int right = mPointInfo.rect.right;
        canvas.drawLine(left, cy, right, cy, mPaint); // vertical line

        // date
        String date = SimpleDrawDate.DATE_FORMAT.format(data.getDate());
        float textSize = mKlineView.sp2Px(FONT_SIZE);
        int padding = (int) mKlineView.dp2Px(TEXT_PADDING);
        float radius = mKlineView.dp2Px(2);
        DateDrawArea dateDrawArea = null;
        for (DrawArea drawArea : mKlineView.getDrawAreaList()) {
            if (drawArea instanceof DateDrawArea) {
                dateDrawArea = (DateDrawArea) drawArea;
                break;
            }
        }
        if (dateDrawArea != null) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(textSize);
            float textWidth = mPaint.measureText(date);
            float textTop = dateDrawArea.getTop() + padding;
            float textLeft = cx - textWidth / 2;
            float textRight = cx + textWidth / 2;
            if (textLeft < dateDrawArea.getLeft() + padding) {
                textLeft = dateDrawArea.getLeft() + padding;
            }
            if (textRight > dateDrawArea.getLeft() + dateDrawArea.getWidth() - padding) {
                textRight = dateDrawArea.getLeft() + dateDrawArea.getWidth() - padding;
                textLeft = textRight - textWidth;
            }
            DrawTextTool.getTextBgFromLeftTop(date, textLeft, textTop, padding, padding, mBgRectF, mPaint);
            mPaint.setColor(mLineColor);
            radius = mKlineView.dp2Px(2);
            canvas.drawRoundRect(mBgRectF, radius, radius, mPaint);
            mPaint.setColor(mTextColor);
            DrawTextTool.drawTextFromLeftTop(date, textLeft, textTop, canvas, mPaint);
        }

        // number
        if (mDrawArea instanceof IndicatorDrawArea) {
            float number = ((IndicatorDrawArea) mDrawArea).getNumber(cy);
            float max = ((IndicatorDrawArea) mDrawArea).getCurIndicator().getAuxiliaryLines().getMax();
            int numberScale = FloatCalc.get().getScale(max);
            numberScale = numberScale != FloatCalc.get().getFormatScale().getMinScale()
                    ? FloatCalc.get().getFormatScale().getMaxScale()
                    : FloatCalc.get().getFormatScale().getMinScale();
            String numStr = FloatCalc.get().format(number, numberScale);
            float textWidth = mPaint.measureText(numStr);
            float textLeft = mDrawArea.getLeft() + padding;
            if (mCrossPointInfo.visibleIndex < mKlineView.getCandles() / 2) {
                textLeft = mDrawArea.getLeft() + mDrawArea.getWidth() - padding - textWidth;
            }
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float textHeight = fontMetrics.bottom - fontMetrics.top;
            float textTop = cy - textHeight / 2;
            DrawTextTool.getTextBgFromLeftTop(numStr, textLeft, textTop, padding, padding, mBgRectF, mPaint);
            mPaint.setColor(mLineColor);
            canvas.drawRoundRect(mBgRectF, radius, radius, mPaint);
            mPaint.setColor(mTextColor);
            DrawTextTool.drawTextFromLeftTop(numStr, textLeft, textTop, canvas, mPaint);
        }

        for (DrawArea drawArea : mIndicatorTextDrawAreaList) {
            if (drawArea instanceof IndicatorTextDrawArea) {
                ((IndicatorTextDrawArea) drawArea).draw(mKlineView, data, canvas, mPaint);
            }
        }
    }
}
