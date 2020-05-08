package com.johnzh.klinelib;

import androidx.annotation.NonNull;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class KlineCalc {

    private int width;
    private int dataHeight;
    private int dateHeight;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private int startIndex;
    private int endIndex;

    private float maxCandleWidth;
    private float maxDragDistance;

    private AuxiliaryLines auxiliaryLines;

    void init(int width, int dataHeight, int dateHeight,
              int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.width = width;
        this.dataHeight = dataHeight;
        this.dateHeight = dateHeight;
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
    }

    void calcMaxCandleWidth(int candles) {
        maxCandleWidth = (width - paddingLeft - paddingRight) / candles;
    }

    void calcMaxDragDistance(int dateListSize, int candles) {
        maxDragDistance = Math.max((dateListSize - candles) * maxCandleWidth, 0);
    }

    void setAuxiliaryLines(@NonNull AuxiliaryLines auxiliaryLines) {
        this.auxiliaryLines = auxiliaryLines;
    }

    void setStartIndex(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    protected float getPriceY(float price) {
        float max = auxiliaryLines.getMax();
        float min = auxiliaryLines.getMin();
        if (price < min || price > max) {
            return -1;
        }
        float offset = (max - price) / (max - min) * dateHeight;
        return paddingTop + offset;
    }

    protected float getCandleMidX(int dataIndex) {
        int visibleIndex = dataIndex - startIndex;
        float midOfCandle = maxCandleWidth / 2;
        return paddingLeft + visibleIndex * maxCandleWidth + midOfCandle;
    }

    protected int getDataIndex(float candleMidX) {
        float midOfCandle = maxCandleWidth / 2;
        int visibleIndex = (int) ((candleMidX - paddingLeft - midOfCandle) / maxCandleWidth);
        return visibleIndex + startIndex;
    }

    protected float getDateY() {
        return 0;
    }

    public int getNoPaddingWidth() {
        return width - paddingLeft - paddingRight;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }
}
