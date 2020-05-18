package com.johnzh.klinelib.drawarea;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.indicators.Indicator;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Modified by john on 2020/5/9
 * <p>
 * Description: Draw area is not contain padding. It is also a class for calculating.
 */
public class IndicatorDrawArea extends RectDrawArea implements YAxisConverter {

    private List<Indicator> mIndicatorList;
    private float mOneDataWidth;
    private int mCurIndex;
    private int mStartIndex;

    @Override
    public void prepareOnDraw(KlineView view, List<DrawArea> allDrawAreas) {
        super.prepareOnDraw(view, allDrawAreas);
        mStartIndex = view.getStartIndex();
        mOneDataWidth = view.getOneDataWidth();
    }

    @Override
    public void calculate(List<DATA> list, int startIndex, int endIndex) {
        getCurIndicator().calcIndex(list, startIndex, endIndex);
        getCurIndicator().calcAuxiliaryLines(list, startIndex, endIndex);
    }

    @Override
    public void draw(KlineView klineView, Canvas canvas, Paint paint) {
        getCurIndicator().drawAuxiliaryLines(klineView, this, canvas, paint);
        getCurIndicator().drawIndex(klineView, this,
                klineView.getStartIndex(), klineView.getEndIndex(),
                canvas, paint);
    }

    public IndicatorDrawArea(int height, @NonNull List<Indicator> indicatorList) {
        super(height);
        mIndicatorList = indicatorList;
        mCurIndex = 0;

        if (mIndicatorList.isEmpty()) {
            throw new IllegalArgumentException("indexList cannot be empty");
        }
    }

    public float getOneDataWidth() {
        return mOneDataWidth;
    }

    public Indicator getCurIndicator() {
        return mIndicatorList.get(mCurIndex);
    }

    public void selectIndicator(int indexesIndex) {
        if (indexesIndex < 0 || indexesIndex >= mIndicatorList.size()) return;
        if (indexesIndex != mCurIndex) {
            mCurIndex = indexesIndex;
        }
    }

    public List<Indicator> getIndicatorList() {
        return mIndicatorList;
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

    @Override
    public float getNumber(float drawY) {
        AuxiliaryLines auxiliaryLines = getCurIndicator().getAuxiliaryLines();
        float max = auxiliaryLines.getMaximum();
        float min = auxiliaryLines.getMinimum();
        return max - (drawY - top) / height * (max - min);
    }

    @Override
    public float getDrawY(float number) {
        AuxiliaryLines auxiliaryLines = getCurIndicator().getAuxiliaryLines();
        float max = auxiliaryLines.getMaximum();
        float min = auxiliaryLines.getMinimum();
        if (number < min || number > max) {
            return -1;
        }
        float offset = (max - number) / (max - min) * height;
        return top + offset;
    }
}
