package com.johnzh.klinelib;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.size.ViewSize;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class DefaultDrawArea implements DrawArea {

    private int viewWidth;
    private int viewHeight;
    private int dataViewHeight;
    private int dateViewHeight;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private float oneDataWidth;
    private AuxiliaryLines auxiliaryLines;

    @Override
    public void init(KlineView klineView, ViewSize viewSize, AuxiliaryLines auxiliaryLines) {
        this.viewWidth = klineView.getWidth();
        this.viewHeight = klineView.getHeight();
        this.dataViewHeight = viewSize.getDataViewHeight();
        this.dateViewHeight = viewSize.getDateViewHeight();
        this.paddingLeft = klineView.getPaddingLeft();
        this.paddingTop = klineView.getPaddingTop();
        this.paddingRight = klineView.getPaddingRight();
        this.paddingBottom = klineView.getPaddingBottom();
        this.auxiliaryLines = auxiliaryLines;
    }

    @Override
    public float calcOneDataWidth(int visibleDataSize) {
        oneDataWidth = (viewWidth - paddingLeft - paddingRight) * 1.0f / visibleDataSize;
        return oneDataWidth;
    }

    @Override
    public float getOneDataWidth() {
        return oneDataWidth;
    }

    @Override
    public int getDataIndex(int visibleIndex, int startIndex) {
        return visibleIndex + startIndex;
    }

    @Override
    public int getVisibleIndex(int dataIndex, int startIndex) {
        return dataIndex - startIndex;
    }

    @Override
    public float getDrawX(int visibleIndex) {
        float midOfDataWidth = oneDataWidth / 2;
        return paddingLeft + visibleIndex * oneDataWidth + midOfDataWidth;
    }

    @Override
    public float getDrawY(float number) {
        float max = auxiliaryLines.getMaximum();
        float min = auxiliaryLines.getMinimum();
        if (number < min || number > max) {
            return -1;
        }
        float offset = (max - number) / (max - min) * dataViewHeight;
        return paddingTop + offset;
    }

    @Override
    public int getVisibleIndex(float drawX) {
        float midOfDistance = oneDataWidth / 2;
        int visibleIndex = (int) ((drawX - paddingLeft - midOfDistance) / oneDataWidth);
        return visibleIndex;
    }

    @Override
    public float getNumber(float drawY) {
        float max = auxiliaryLines.getMaximum();
        float min = auxiliaryLines.getMinimum();
        return max - (drawY - paddingTop) / dataViewHeight * (max - min);
    }

    @Override
    public int getTop() {
        return paddingTop;
    }

    @Override
    public int getLeft() {
        return paddingLeft;
    }

    @Override
    public int getWidth() {
        return viewWidth - paddingLeft - paddingRight;
    }

    @Override
    public int getHeight() {
        return viewHeight - paddingTop - paddingBottom;
    }

    @Override
    public int getDateTop() {
        return dataViewHeight;
    }
}
