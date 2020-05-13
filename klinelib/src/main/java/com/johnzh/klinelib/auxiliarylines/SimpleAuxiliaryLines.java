package com.johnzh.klinelib.auxiliarylines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.indexes.Index;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public class SimpleAuxiliaryLines implements AuxiliaryLines<KlineData> {

    @Override
    public void calcHorizontalLines(List<KlineData> klineDataList, Index<KlineData> curIndex, int startIndex, int endIndex) {

    }

    @Override
    public void calcVerticalLines(List<KlineData> klineDataList, int startIndex, int endIndex) {

    }

    @Override
    public void drawHorizontalLines(KlineView klineView, Canvas canvas, Paint paint) {

    }

    @Override
    public void drawVerticalLines(KlineView klineView, Canvas canvas, Paint paint) {

    }

    @Override
    public float getMaximum() {
        return 0;
    }

    @Override
    public float getMinimum() {
        return 0;
    }
}
