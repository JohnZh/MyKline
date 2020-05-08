package com.johnzh.klinelib;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description:
 */
public class HorizontalAuxiliaryLines implements AuxiliaryLines {

    private float[] horizontalLines;

    public HorizontalAuxiliaryLines() {
        this.horizontalLines = new float[4];
    }

    @Override
    public void calcHorizontalLines(List<KlineData> klineDataList, Index curIndex, int startIndex, int endIndex) {
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        for (int i = startIndex; i < endIndex; i++) {
            KlineData data = klineDataList.get(i);
            max = Math.max(max, data.getHighestPrice());
            min = Math.min(min, data.getLowestPrice());
        }

        if (max == Float.MIN_VALUE || min == Float.MAX_VALUE) return;

        float priceRange = (max - min) / (horizontalLines.length - 1);

        if (priceRange == 0) { // max == min
            priceRange = 1 / (horizontalLines.length - 1);
        }

        horizontalLines[0] = max;
        horizontalLines[horizontalLines.length - 1] = min;
        for (int i = horizontalLines.length - 2; i > 0; i--) {
            horizontalLines[i] = horizontalLines[i + 1] + priceRange;
        }
    }

    @Override
    public void calcVerticalLines(List<KlineData> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void drawHorizontalLines(KlineCalc klineCalc, Canvas canvas, Paint sPaint) {

    }

    @Override
    public void drawVerticalLines(KlineCalc klineCalc, Canvas canvas, Paint sPaint) {

    }

    @Override
    public float getMax() {
        return 0;
    }

    @Override
    public float getMin() {
        return 0;
    }
}
