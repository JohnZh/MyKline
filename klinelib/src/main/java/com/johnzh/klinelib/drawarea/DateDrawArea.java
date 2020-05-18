package com.johnzh.klinelib.drawarea;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.date.DrawDate;

import java.util.List;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public class DateDrawArea extends RectDrawArea {

    private DrawDate mDrawDate;
    private int mStartIndex;
    private float mOneDataWidth;


    public DateDrawArea(int height, DrawDate drawDate) {
        super(height);
        mDrawDate = drawDate;
    }

    @Override
    public void prepareOnDraw(KlineView view, List<DrawArea> allDrawAreas) {
        super.prepareOnDraw(view, allDrawAreas);
        mStartIndex = view.getStartIndex();
        mOneDataWidth = view.getOneDataWidth();
    }

    @Override
    public void calculate(List<DATA> list, int startIndex, int endIndex) {
    }

    @Override
    public void draw(KlineView klineView, Canvas canvas, Paint paint) {
        mDrawDate.drawDate(klineView, this,
                klineView.getStartIndex(), klineView.getEndIndex(),
                canvas, paint);
    }

    public DrawDate getDrawDate() {
        return mDrawDate;
    }

    @Override
    public int getDataIndex(int visibleIndex) {
        return visibleIndex + mStartIndex;
    }

    @Override
    public int getVisibleIndex(int dataIndex) {
        return dataIndex - mStartIndex;
    }

    @Override
    public int getVisibleIndex(float drawX) {
        float midOfDistance = mOneDataWidth / 2;
        int visibleIndex = (int) ((drawX - left - midOfDistance) / mOneDataWidth);
        return visibleIndex;
    }

    @Override
    public float getDrawX(int visibleIndex) {
        float midOfDataWidth = mOneDataWidth / 2;
        return left + visibleIndex * mOneDataWidth + midOfDataWidth;
    }
}
