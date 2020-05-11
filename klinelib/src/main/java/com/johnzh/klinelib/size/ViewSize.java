package com.johnzh.klinelib.size;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: two parts of view, data part and date part
 */
public abstract class ViewSize {

    protected int viewWidth;
    protected int dataViewHeight;
    protected int dateViewHeight;

    public ViewSize() {
    }

    public ViewSize(int viewWidth, int dataViewHeight, int dateViewHeight) {
        this.viewWidth = viewWidth;
        this.dataViewHeight = dataViewHeight;
        this.dateViewHeight = dateViewHeight;
    }

    public ViewSize(int dataViewHeight, int dateViewHeight) {
        this.dataViewHeight = dataViewHeight;
        this.dateViewHeight = dateViewHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getHeight() {
        return dataViewHeight + dateViewHeight;
    }

    public int getDataViewHeight() {
        return dataViewHeight;
    }

    public int getDateViewHeight() {
        return dateViewHeight;
    }
}
