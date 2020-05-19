package com.johnzh.klinelib.drawarea.impl;

import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.drawarea.DrawArea;

import java.util.List;

/**
 * Created by JohnZh on 2020/5/19
 *
 * <p>A abstract function draw area which is used to convert index and x axis</p>
 */
public abstract class ConvertDrawArea extends RectDrawArea {

    protected float mOneDataWidth;
    protected int mStartIndex;

    public ConvertDrawArea() {
    }

    public ConvertDrawArea(int height) {
        super(height);
    }

    public ConvertDrawArea(int width, int height) {
        super(width, height);
    }

    public float getOneDataWidth() {
        return mOneDataWidth;
    }

    public int getStartIndex() {
        return mStartIndex;
    }

    @Override
    public void prepareOnDraw(KlineView view, List<DrawArea> allDrawAreas) {
        super.prepareOnDraw(view, allDrawAreas);
        mStartIndex = view.getStartIndex();
        mOneDataWidth = view.getOneDataWidth();
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
