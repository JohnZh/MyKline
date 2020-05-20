package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.drawarea.impl.IndicatorDrawArea;

import java.util.List;

/**
 * Created by JohnZh on 2020/5/21
 *
 * <p>WR indicator</p>
 */
public class WRIndicator extends AbsIndicator {
    private final float textSize;
    private final int color;
    private final float textMargin;

    public WRIndicator(AuxiliaryLines auxiliaryLines, int[] wr, float textSize, int color, float textMargin) {
        super(auxiliaryLines);
        this.textSize = textSize;
        this.color = color;
        this.textMargin = textMargin;
    }

    @Override
    public void calcIndicatorAsync(List<DATA> dataList) {

    }

    @Override
    public void calcIndicator(List<DATA> dataList, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {

        }
    }

    @Override
    public void drawIndicator(KlineView klineView, IndicatorDrawArea drawArea, Canvas canvas, Paint paint) {

    }

    @Override
    public void drawIndicatorText(KlineView klineView, DrawArea drawArea, DATA data, Canvas canvas, Paint paint) {

    }
}
