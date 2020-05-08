package com.johnzh.klinelib.size;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: two parts of view, data part and date part
 */
public abstract class ViewSize {

    protected int width;
    protected int dataHeight;
    protected int dateHeight;

    public ViewSize() {
    }

    public ViewSize(int width, int dataHeight, int dateHeight) {
        this.width = width;
        this.dataHeight = dataHeight;
        this.dateHeight = dateHeight;
    }

    public ViewSize(int dataHeight, int dateHeight) {
        this.dataHeight = dataHeight;
        this.dateHeight = dateHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return dataHeight + dateHeight;
    }

    public int getDataHeight() {
        return dataHeight;
    }

    public int getDateHeight() {
        return dateHeight;
    }
}
