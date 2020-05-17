package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.IndexDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public abstract class AbsIndex<T extends KlineData> implements Index<T> {

    private AuxiliaryLines<T> auxiliaryLines;

    public AbsIndex(AuxiliaryLines<T> auxiliaryLines) {
        this.auxiliaryLines = auxiliaryLines;
    }

    @Override
    public void calcAuxiliaryLines(List<T> klineDataList, int startIndex, int endIndex) {
        this.auxiliaryLines.calcHorizontalLines(klineDataList, this, startIndex, endIndex);
        this.auxiliaryLines.calcVerticalLines(klineDataList, startIndex, endIndex);
    }

    @Override
    public void drawAuxiliaryLines(KlineView klineView, IndexDrawArea drawArea, Canvas canvas, Paint paint) {
        this.auxiliaryLines.drawHorizontalLines(klineView, drawArea, canvas, paint);
        this.auxiliaryLines.drawVerticalLines(klineView, drawArea, canvas, paint);
    }

    @Override
    public AuxiliaryLines<T> getAuxiliaryLines() {
        return auxiliaryLines;
    }
}
