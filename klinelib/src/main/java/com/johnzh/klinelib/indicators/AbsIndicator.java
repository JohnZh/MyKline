package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public abstract class AbsIndicator implements Indicator {

    private AuxiliaryLines auxiliaryLines;

    private float max;
    private float min;

    public AbsIndicator(AuxiliaryLines auxiliaryLines) {
        this.auxiliaryLines = auxiliaryLines;
        this.max = Float.MIN_VALUE;
        this.min = Float.MAX_VALUE;
    }

    @Override
    public void calcIndicatorAsync(List<DATA> dataList) {

    }

    /**
     * Method to calculate indicator value range.<br/>
     *
     * Use #{@link this#updateMaxMin(float)} or #{@link this#updateMaxMin(float, float)} to update value range
     * @param dataList
     * @param startIndex
     * @param endIndex
     */
    protected abstract void calcMaxMinPreCalcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex);

    @Override
    public void calcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex) {
        calcMaxMinPreCalcAuxiliaryLines(dataList, startIndex, endIndex);
        this.auxiliaryLines.calcHorizontalLines(dataList, this, startIndex, endIndex);
        this.auxiliaryLines.calcVerticalLines(dataList, startIndex, endIndex);
    }

    @Override
    public void drawAuxiliaryLines(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {
        this.auxiliaryLines.drawHorizontalLines(klineView, drawArea, canvas, paint);
        this.auxiliaryLines.drawVerticalLines(klineView, drawArea, canvas, paint);
    }

    @Override
    public AuxiliaryLines getAuxiliaryLines() {
        return auxiliaryLines;
    }

    @Override
    public float getMaximum() {
        return max;
    }

    @Override
    public float getMinimum() {
        return min;
    }

    protected void resetMaxMin() {
        max = Float.MIN_VALUE;
        min = Float.MAX_VALUE;
    }

    protected void updateMaxMin(float number) {
        max = Math.max(max, number);
        min = Math.min(min, number);
    }

    protected void updateMaxMin(float max, float min) {
        this.max = Math.max(this.max, max);
        this.min = Math.min(this.min, min);
    }
}
