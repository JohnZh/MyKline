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
    private float distanceBetweenData;
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
    public float getDistanceBetweenData(int visibleDataSize) {
        distanceBetweenData = (viewWidth - paddingLeft - paddingRight) * 1.0f / visibleDataSize;
        return distanceBetweenData;
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
    public float getDataX(int visibleIndex) {
        float midOfDistance = distanceBetweenData / 2;
        return paddingLeft + visibleIndex * distanceBetweenData + midOfDistance;
    }

    @Override
    public float getDataY(float price) {
        float max = auxiliaryLines.getMaximum();
        float min = auxiliaryLines.getMinimum();
        if (price < min || price > max) {
            return -1;
        }
        float offset = (max - price) / (max - min) * dataViewHeight;
        return paddingTop + offset;
    }

    @Override
    public int getVisibleIndex(float touchX) {
        float midOfDistance = distanceBetweenData / 2;
        int visibleIndex = (int) ((touchX - paddingLeft - midOfDistance) / distanceBetweenData);
        return visibleIndex;
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
