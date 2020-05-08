package com.johnzh.klinelib;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: generally, horizontal lines are about price, index. vertical lines are about date
 */
public interface AuxiliaryLines {

    void calcHorizontalLines(List<KlineData> klineDataList, Index curIndex, int startIndex, int endIndex);

    void calcVerticalLines(List<KlineData> klineDataList, int startIndex, int endIndex);

    void drawHorizontalLines(KlineCalc klineCalc, Canvas canvas, Paint sPaint);

    void drawVerticalLines(KlineCalc klineCalc, Canvas canvas, Paint sPaint);

    float getMax();

    float getMin();
}