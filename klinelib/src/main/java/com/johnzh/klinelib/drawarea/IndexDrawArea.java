package com.johnzh.klinelib.drawarea;

import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.indexes.Index;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Modified by john on 2020/5/9
 * <p>
 * Description: Draw area is not contain padding. It is also a class for calculating.
 */
public class IndexDrawArea extends SizedDrawArea
        implements DataIndexConverter, CoordinateConverter {

    private List<Index> mIndexList;
    private float mOneDataWidth;
    private int mCurIndex;
    private int mStartIndex;

    @Override
    public void initOnDraw(KlineView view, List<DrawArea> allDrawAreas) {
        super.initOnDraw(view, allDrawAreas);
        mStartIndex = view.getStartIndex();
        mOneDataWidth = view.getOneDataWidth();
    }

    public IndexDrawArea(int height, @NonNull List<Index> indexList) {
        super(height);
        mIndexList = indexList;
        mCurIndex = 0;

        if (mIndexList.isEmpty()) {
            throw new IllegalArgumentException("indexList cannot be empty");
        }
    }

    public float getOneDataWidth() {
        return mOneDataWidth;
    }

    public Index getCurIndex() {
        return mIndexList.get(mCurIndex);
    }

    public void selectIndex(int indexOfIndexes) {
        if (indexOfIndexes < 0 || indexOfIndexes >= mIndexList.size()) return;
        if (indexOfIndexes != mCurIndex) {
            mCurIndex = indexOfIndexes;
        }
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
    public float getNumber(float drawY) {
        AuxiliaryLines auxiliaryLines = getCurIndex().getAuxiliaryLines();
        float max = auxiliaryLines.getMaximum();
        float min = auxiliaryLines.getMinimum();
        return max - (drawY - top) / height * (max - min);
    }

    @Override
    public float getDrawX(int visibleIndex) {
        float midOfDataWidth = mOneDataWidth / 2;
        return left + visibleIndex * mOneDataWidth + midOfDataWidth;
    }

    @Override
    public float getDrawY(float number) {
        AuxiliaryLines auxiliaryLines = getCurIndex().getAuxiliaryLines();
        float max = auxiliaryLines.getMaximum();
        float min = auxiliaryLines.getMinimum();
        if (number < min || number > max) {
            return -1;
        }
        float offset = (max - number) / (max - min) * height;
        return top + offset;
    }
}
