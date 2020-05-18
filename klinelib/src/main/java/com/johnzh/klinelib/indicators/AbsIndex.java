package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.IndexDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public abstract class AbsIndex implements Index {

    private AuxiliaryLines auxiliaryLines;

    public AbsIndex(AuxiliaryLines auxiliaryLines) {
        this.auxiliaryLines = auxiliaryLines;
    }

    @Override
    public void calcAuxiliaryLines(List<DATA> dataList, int startIndex, int endIndex) {
        this.auxiliaryLines.calcHorizontalLines(dataList, this, startIndex, endIndex);
        this.auxiliaryLines.calcVerticalLines(dataList, startIndex, endIndex);
    }

    @Override
    public void drawAuxiliaryLines(KlineView klineView, IndexDrawArea drawArea, Canvas canvas, Paint paint) {
        this.auxiliaryLines.drawHorizontalLines(klineView, drawArea, canvas, paint);
        this.auxiliaryLines.drawVerticalLines(klineView, drawArea, canvas, paint);
    }

    @Override
    public AuxiliaryLines getAuxiliaryLines() {
        return auxiliaryLines;
    }
}
